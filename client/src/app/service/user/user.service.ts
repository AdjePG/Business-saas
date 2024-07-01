// user.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { jwtDecode }  from 'jwt-decode';
import { User } from 'src/app/models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);

  constructor() {
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
}
