import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from 'src/app/models/api-response.model';

@Injectable({
  	providedIn: 'root'
})
export class AuthService {
	private apiUrl = 'http://127.0.0.1:8080/api/users/';

	constructor(
		private http: HttpClient
	) { 
		
	}

	signUp(userData: any): Observable<ApiResponse<void>> {
		return this.http.post<ApiResponse<void>>(
			`${this.apiUrl}signup`, 
			userData
		);
	}

	logIn(userData: any): Observable<any> {
		const headers = new HttpHeaders({
			'Content-Type': 'application/json',
		});

		return this.http.post(`${this.apiUrl}login`, userData,
			{
				headers,
				withCredentials: true
			}
		);
	}

	isLoggedIn(): Observable<any> {
		console.log("Mirar isLoggedIn")
		const headers = new HttpHeaders({
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
		});

		return this.http.get(
			`${this.apiUrl}is-logged`,
			{
				headers,
				withCredentials: true
			}
		);
	}
}
