import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../service/auth/auth.service';

export const authGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const router: Router = inject(Router)
  const authService = inject(AuthService)

  const isLogged = new Promise<boolean>((resolve, reject) => {
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
          resolve(false);
        } else {
          resolve(true);
        }
      }
    });
  });

  return isLogged
}