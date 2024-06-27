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

  private apiUrl = 'http://127.0.0.1:8080/api/business';

  constructor(private http: HttpClient) { }


  getAllBusinesses(userId?: number): Observable<Business[]> {
    let url = this.apiUrl+'/businesses';
    if (userId) {
      url += `?userId=${userId}`;
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.get<ApiResponse<Business[]>>(url, { headers }).pipe(
      map((response: ApiResponse<Business[]>) => response.result)
    );
  }

  /*


getAllBusinesses(userId?: number): Observable<Business[]> {
    let url = this.apiUrl;
    if (userId) {
      url += `?userId=${userId}`;
    }
    return this.http.get<Business[]>(url);
  }

  getBusinessById(uuid: number): Observable<Business> {
    return this.http.get<Business>(`${this.apiUrl}/${uuid}`);
  }

  createBusiness(business: Business): Observable<Business> {
    return this.http.post<Business>(this.apiUrl, business);
  }

  */
}
