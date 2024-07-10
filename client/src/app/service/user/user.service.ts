import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { jwtDecode }  from 'jwt-decode';
import { User } from 'src/app/models/user';
import { ApiResponse } from 'src/app/models/api-response.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
  private apiUrl = 'http://127.0.0.1:8080/api/users/';

  constructor(private http: HttpClient) {
    this.setUserData();
  }

  private setUserData(): void {
    const token = localStorage.getItem('user-auth');
    const userData = token ? this.getUserDataFromToken(token) : null;
    this.userSubject.next(userData);

    if (!userData?.uuid) {
      console.error('No se pudo obtener los datos del usuario');
    }
  }

  clearUserData(): void {
    this.userSubject.next(null);
  }

  private getUserDataFromToken(token: string): User | null {
    try {
      const decoded: any = jwtDecode(token);
      return {
        uuid: decoded?.uuid || null,
        email: decoded?.email || null
      };
    } catch (error) {
      console.error('Error decoding token', error);
      return null;
    }
  }

  getUserData(): Observable<User | null> {
    return this.userSubject.asObservable();
  }

  getUserDataSync(): User | null {
    return this.userSubject.value;
  }

  getUser(uuid: String): Observable<ApiResponse<User>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.get<ApiResponse<User>>(
      `${this.apiUrl}${uuid}`,
      { 
        headers 
      }
    );
  }

  updateUser(user: User): Observable<ApiResponse<void>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.put<ApiResponse<void>>(
      `${this.apiUrl}`, 
      user, 
      { 
        headers 
      }
    );
  }

  deleteUser(): Observable<ApiResponse<void>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.delete<ApiResponse<void>>(
      `${this.apiUrl}`,
      { 
        headers 
      }
    );
  }
}
