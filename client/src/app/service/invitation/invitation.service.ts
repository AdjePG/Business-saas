import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from 'src/app/models/api-response.model';
import { BusinessInvitation } from 'src/app/models/business-invitation';

@Injectable({
  	providedIn: 'root'
})
export class InvitationService {
  	private apiUrl = 'http://127.0.0.1:8080/api/invitations/';

	constructor(
		private http: HttpClient
	) { 

	}
	
	shareBusiness(uuid: string, sharedBusiness: BusinessInvitation): Observable<ApiResponse<void>> {
		const headers = new HttpHeaders({
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
		});

		return this.http.post<ApiResponse<void>>(
			`${this.apiUrl}share-business/${uuid}`, 
			sharedBusiness,
			{ 
				headers 
			}
		);
	}

	businessInvitationAccepted(invitationToken?: string): Observable<ApiResponse<void>> {
		const headers = new HttpHeaders({
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
		});

		return this.http.post<ApiResponse<void>>(
			`${this.apiUrl}business-invit-accept/${invitationToken}`,
			invitationToken,
			{ 
				headers, 
				withCredentials: true 
			}
		);
	}
}
