import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Business } from 'src/app/models/business';
import { ApiResponse  } from 'src/app/models/api-response.model';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BusinessService {
  private apiUrl = 'http://127.0.0.1:8080/api/businesses/';

  constructor(private http: HttpClient) { }

  getAllBusinesses(userId?: string): Observable<Business[]> {
    let url = this.apiUrl;
    if (userId) {
      url += `?userId=${userId}`;
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.get<ApiResponse<Business[]>>(url, { headers, withCredentials: true }).pipe(
      map((response: ApiResponse<Business[]>) => response.result)
    );
  }

  createBusiness(business: Business): Observable<ApiResponse<Business>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.post<ApiResponse<Business>>(this.apiUrl, business, { headers });
  }

  updateBusiness(uuid: string, business: Business): Observable<ApiResponse<Business>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.put<ApiResponse<Business>>(`${this.apiUrl}${uuid}`, business, { headers });
  }

  getBusinessById(uuid: string): Observable<Business> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.get<Business>(`${this.apiUrl}${uuid}`, { headers });
  }

  deleteBusiness(id: string): Observable<void> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.delete<void>(`${this.apiUrl}${id}`, { headers });
  }
}
