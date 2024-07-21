import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Business } from 'src/app/models/business';
import { ApiResponse  } from 'src/app/models/api-response.model';

@Injectable({
 	providedIn: 'root'
})
export class BusinessService {
	private apiUrl = 'http://127.0.0.1:8080/api/businesses/';

	constructor(
		private http: HttpClient
	) { 

	}

	getBusinesses(userUuid?: string, shared: boolean = false): Observable<ApiResponse<Business[]>> {
		let url = this.apiUrl;
		
		if (userUuid) {
			url += `?userUuid=${userUuid}`;

			if (shared) {
				url += `&shared=${shared}`;
			}
		}

		const headers = new HttpHeaders({
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
		});

		return this.http.get<ApiResponse<Business[]>>(
			url, 
			{ 
				headers, 
				withCredentials: true 
			}
		);
	}

	createBusiness(business: Business): Observable<ApiResponse<Business>> {
		const headers = new HttpHeaders({
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
		});

		return this.http.post<ApiResponse<Business>>(
			this.apiUrl, 
			business, 
			{ 
				headers 
			}
		);
	}

	updateBusiness(business: Business): Observable<ApiResponse<Business>> {
		const headers = new HttpHeaders({
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
		});

		return this.http.put<ApiResponse<Business>>(
			`${this.apiUrl}${business.uuid}`, 
			business, 
			{ 
				headers 
			}
		);
	}

	deleteBusiness(uuid: string, shared: boolean = false): Observable<ApiResponse<void>> {
		let url = `${this.apiUrl}${uuid}`;
		
		if (shared) {
			url += `?shared=${shared}`;
		}

		const headers = new HttpHeaders({
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
		});

		return this.http.delete<ApiResponse<void>>(
			url, 
			{ 
				headers 
			}
		);
	}

	getBusinessById(uuid: string): Observable<ApiResponse<Business>> {
		const headers = new HttpHeaders({
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
		});

		return this.http.get<ApiResponse<Business>>(
			`${this.apiUrl}${uuid}`, 
			{ 
				headers 
			}
		);
	}
}
