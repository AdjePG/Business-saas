import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://127.0.0.1:8080/api/users/';

  constructor(private http: HttpClient) { }

  signUp(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}signup`, userData);
  }

  logIn(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}login`, userData,
      {
        headers: {
          'Content-Type': 'application/json'
        },
        withCredentials: true
      }
    );
  }

  isLoggedIn(): Observable<any> {
    return this.http.get(`${this.apiUrl}is-logged`,
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
        },
        withCredentials: true
      }
    );
  }
}
