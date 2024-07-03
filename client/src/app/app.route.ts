import { Routes } from '@angular/router';
import { AppLayout } from './layouts/app-layout/app-layout';
import { AuthLayout } from './layouts/auth-layout/auth-layout';
import { authGuard } from './shared/auth-guard';
import { ProfileComponent } from './pages/profile/profile.component';
import { CalendarComponent } from './pages/calendar/calendar.component';

export const routes: Routes = [
    {
        path: '',
        component: AppLayout,
        children: [
            { 
                path: '', 
                loadChildren: () => import('./pages/home/home.module').then((d) => d.HomeModule) 
            },
            { 
                path: 'profile', 
                component: ProfileComponent 
            },
            { 
                path: 'calendar', 
                component: CalendarComponent 
            }
        ],
        canActivate: [authGuard]
    },
    {
        path: '',
        component: AuthLayout,
        children: [
            { 
                path: 'auth', 
                loadChildren: () => import('./pages/auth/auth.module').then((d) => d.AuthModule) 
            },
        ],
        canActivate: [authGuard]
    },
    {
        path: ':id',
        component: AppLayout,
        children: [
            { 
                path: '', 
                loadChildren: () => import('./pages/dashboard/dashboard.module').then((d) => d.DashboardModule) 
            }
        ],
        canActivate: [authGuard]
    },
    {
        path: '**',
        redirectTo: ''
    }
];
