import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AdminService } from '../../services/admin.service';
import { User, AuditLog, PageResponse } from '../../models';
import { take } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  currentUser: User | null = null;
  isAdmin = false;
  isLoading = true;

  // Admin data
  users: User[] = [];
  auditLogs: AuditLog[] = [];
  usersPage = { current: 0, total: 0, size: 10 };
  auditPage = { current: 0, total: 0, size: 20 };

  // Statistics
  stats = {
    totalUsers: 0,
    activeUsers: 0,
    totalAuditLogs: 0,
    recentLogins: 0
  };

  activeTab: 'overview' | 'users' | 'audit' = 'overview';

  constructor(
    private authService: AuthService,
    private adminService: AdminService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCurrentUser();
  }

  loadCurrentUser(): void {
    // Get the current value from BehaviorSubject (only once)
    this.authService.currentUser$.pipe(take(1)).subscribe({
      next: (user) => {
        if (user) {
          this.currentUser = user;
          this.isAdmin = this.authService.isAdmin();
          this.isLoading = false;

          if (this.isAdmin) {
            this.loadAdminData();
          }
        } else {
          // If no user in BehaviorSubject, user should have been set in login
          // This shouldn't happen, but if it does, just use localStorage data
          console.warn('No user in currentUser$, using token from localStorage');
          this.isLoading = false;
        }
      }
    });
  }

  loadAdminData(): void {
    this.loadUsers();
    this.loadAuditLogs();
  }

  loadUsers(page: number = 0): void {
    console.log('[DEBUG] Loading users from admin API');
    this.adminService.getAllUsers(page, this.usersPage.size).subscribe({
      next: (response: PageResponse<User>) => {
        console.log('[DEBUG] Users loaded:', response);
        this.users = response.content;
        this.usersPage.current = response.number;
        this.usersPage.total = response.totalPages;
        this.stats.totalUsers = response.totalElements;
        this.stats.activeUsers = response.content.filter(u => u.enabled).length;
      },
      error: (error) => {
        console.error('[ERROR] Error loading users:', error);
        console.error('[ERROR] Status:', error.status);
        console.error('[ERROR] Message:', error.error?.message || error.message);
      }
    });
  }

  loadAuditLogs(page: number = 0): void {
    console.log('[DEBUG] Loading audit logs from admin API');
    this.adminService.getAuditLogs(page, this.auditPage.size).subscribe({
      next: (response: PageResponse<AuditLog>) => {
        console.log('[DEBUG] Audit logs loaded:', response);
        this.auditLogs = response.content;
        this.auditPage.current = response.number;
        this.auditPage.total = response.totalPages;
        this.stats.totalAuditLogs = response.totalElements;
        this.stats.recentLogins = response.content.filter(log => log.action === 'LOGIN').length;
      },
      error: (error) => {
        console.error('[ERROR] Error loading audit logs:', error);
        console.error('[ERROR] Status:', error.status);
        console.error('[ERROR] Message:', error.error?.message || error.message);
      }
    });
  }

  setActiveTab(tab: 'overview' | 'users' | 'audit'): void {
    this.activeTab = tab;
  }

  previousUsersPage(): void {
    if (this.usersPage.current > 0) {
      this.loadUsers(this.usersPage.current - 1);
    }
  }

  nextUsersPage(): void {
    if (this.usersPage.current < this.usersPage.total - 1) {
      this.loadUsers(this.usersPage.current + 1);
    }
  }

  previousAuditPage(): void {
    if (this.auditPage.current > 0) {
      this.loadAuditLogs(this.auditPage.current - 1);
    }
  }

  nextAuditPage(): void {
    if (this.auditPage.current < this.auditPage.total - 1) {
      this.loadAuditLogs(this.auditPage.current + 1);
    }
  }

  logout(): void {
    this.authService.logout();
  }

  formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    return date.toLocaleString();
  }
}
