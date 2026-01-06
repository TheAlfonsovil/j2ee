import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { User, PageResponse, AuditLog } from '../models';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient) {}

  getAllUsers(page: number = 0, size: number = 10, sortBy: string = 'id', sortDir: string = 'ASC'): Observable<PageResponse<User>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    return this.http.get<PageResponse<User>>(`${this.apiUrl}/users`, { params });
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/${id}`);
  }

  getAuditLogs(page: number = 0, size: number = 20, username?: string, action?: string): Observable<PageResponse<AuditLog>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (username) {
      params = params.set('username', username);
    }
    if (action) {
      params = params.set('action', action);
    }

    return this.http.get<PageResponse<AuditLog>>(`${this.apiUrl}/audit-logs`, { params });
  }
}
