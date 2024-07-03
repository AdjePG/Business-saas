import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer } from 'src/app/models/customer';
import { ApiResponse  } from 'src/app/models/api-response.model';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://127.0.0.1:8080/api/customers/';

  constructor(private http: HttpClient) { }

  getAllCustomers(userId?: string): Observable<Customer[]> {
    let url = this.apiUrl;
    if (userId) {
      url += `?userId=${userId}`;
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.get<ApiResponse<Customer[]>>(url, { headers, withCredentials: true }).pipe(
      map((response: ApiResponse<Customer[]>) => response.result)
    );
  }

  /*createBusiness(business: Customer): Observable<ApiResponse<Customer>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.post<ApiResponse<Customer>>(this.apiUrl, business, { headers });
  }

  updateBusiness(uuid: string, business: Customer): Observable<ApiResponse<Customer>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.put<ApiResponse<Customer>>(`${this.apiUrl}${uuid}`, business, { headers });
  }

  getBusinessById(uuid: string): Observable<Customer> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.get<Customer>(`${this.apiUrl}${uuid}`, { headers });
  }

  deleteBusiness(id: string): Observable<void> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("user-auth")}`
    });

    return this.http.delete<void>(`${this.apiUrl}${id}`, { headers });
  }*/
}
