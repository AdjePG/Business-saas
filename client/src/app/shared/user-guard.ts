import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../service/auth/auth.service';
import { InvitationService } from '../service/invitation/invitation.service';
import { ApiResponse } from '../models/api-response.model';

export const userGuard: CanActivateFn = async (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const router: Router = inject(Router)
  const authService = inject(AuthService)
  const invitationService = inject(InvitationService)

  const isLogged = await new Promise<boolean>((resolve, reject) => {
    authService.isLoggedIn().subscribe({
      next: isLoggedIn => {
        if (state.url === '/' && !isLoggedIn) {
          router.navigate(['/auth/login']);
          resolve(false);
        } else if ((state.url === '/auth/login' || state.url === '/auth/signup' || state.url === '/auth/lockscreen' || state.url === '/auth/reset-password') && isLoggedIn) {
          router.navigate(['/']);
          resolve(false);
        } else {
          resolve(true);
        }
      },
      error: error => {
        if (state.url !== '/auth/login' && state.url !== '/auth/signup' && state.url !== '/auth/lockscreen' && state.url !== '/auth/reset-password') {
          router.navigate(['/auth/login']);
          reject(false);
        } else {
          reject(true);
        }
      }
    });
  });

  if (isLogged) {
    const invitationToken = localStorage.getItem("invitation-token")

    if (invitationToken) {
      const response = await new Promise<ApiResponse<void>>((resolve, reject) => {
        invitationService.businessInvitationAccepted(invitationToken).subscribe({
          next: response => {
            resolve(response)
          },
          error: error => {
            reject(error.error)
          }
        });
      });
      
      localStorage.removeItem("invitation-token")
      console.log(response)
    }
  }

  return isLogged
}