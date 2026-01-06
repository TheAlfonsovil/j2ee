import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoginRequest, LoginResponse, User } from '../models';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    // Initialize user from localStorage if authenticated
    if (this.isAuthenticated()) {
      const userIdStr = localStorage.getItem('user_id');
      const username = localStorage.getItem('username');
      const rolesStr = localStorage.getItem('roles');
      
      if (userIdStr && username) {
        // Create a minimal user object from localStorage
        const user: User = {
          id: parseInt(userIdStr),
          username: username,
          email: '', // Will be loaded when needed
          fullName: username,
          firstName: username,
          lastName: '',
          role: rolesStr && rolesStr.includes('ADMIN') ? 'ADMIN' : 'USER',
          enabled: true,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        };
        this.currentUserSubject.next(user);
      }
    }
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap(response => {
        this.setSession(response);
        // Set user from login response instead of making another request
        const user: User = {
          id: response.id,
          username: response.username,
          email: response.email,
          fullName: response.fullName,
          firstName: response.fullName?.split(' ')[0] || '',
          lastName: response.fullName?.split(' ').slice(1).join(' ') || '',
          role: (response.roles[0]?.replace('ROLE_', '') || 'USER') as 'USER' | 'ADMIN',
          enabled: true,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        };
        this.currentUserSubject.next(user);
      })
    );
  }

  logout(): void {
    const token = this.getAccessToken();
    if (token) {
      this.http.post(`${this.apiUrl}/auth/logout`, {}).subscribe({
        complete: () => {
          this.clearSession();
        },
        error: () => {
          this.clearSession();
        }
      });
    } else {
      this.clearSession();
    }
  }

  refreshToken(): Observable<LoginResponse> {
    const refreshToken = this.getRefreshToken();
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/refresh`, { refreshToken }).pipe(
      tap(response => {
        this.setSession(response);
      })
    );
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/me`).pipe(
      tap(user => this.currentUserSubject.next(user))
    );
  }

  private loadCurrentUser(): void {
    if (this.isAuthenticated()) {
      this.getCurrentUser().subscribe({
        error: () => {
          // Don't clear session on error, just log it
          console.error('Failed to load current user');
        }
      });
    }
  }

  private setSession(authResult: LoginResponse): void {
    localStorage.setItem('access_token', authResult.accessToken);
    localStorage.setItem('refresh_token', authResult.refreshToken);
    localStorage.setItem('user_id', authResult.id.toString());
    localStorage.setItem('username', authResult.username);
    localStorage.setItem('roles', JSON.stringify(authResult.roles));
  }

  private clearSession(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('user_id');
    localStorage.removeItem('username');
    localStorage.removeItem('roles');
    this.currentUserSubject.next(null);
    // Only navigate to login if not already there
    if (this.router.url !== '/login') {
      this.router.navigate(['/login']);
    }
  }

  getAccessToken(): string | null {
    return localStorage.getItem('access_token');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refresh_token');
  }

  isAuthenticated(): boolean {
    const token = this.getAccessToken();
    return !!token;
  }

  hasRole(role: string): boolean {
    const rolesStr = localStorage.getItem('roles');
    if (!rolesStr) return false;
    const roles: string[] = JSON.parse(rolesStr);
    return roles.includes(role) || roles.includes(`ROLE_${role}`);
  }

  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  isUser(): boolean {
    return this.hasRole('USER');
  }
}
