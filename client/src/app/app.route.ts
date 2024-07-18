import { Routes } from '@angular/router';
import { AppLayout } from './layouts/app-layout/app-layout';
import { AuthLayout } from './layouts/auth-layout/auth-layout';
import { userGuard } from './shared/user-guard';
import { ProfileComponent } from './pages/profile/profile.component';
import { CalendarComponent } from './pages/calendar/calendar.component';
import { InvitationComponent } from './pages/invitation/invitation.component';

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
        canActivate: [userGuard]
    },
    {
        path: 'auth',
        component: AuthLayout,
        children: [
            { 
                path: '', 
                loadChildren: () => import('./pages/auth/auth.module').then((d) => d.AuthModule) 
            },
        ],
        canActivate: [userGuard]
    },
    {
        path: 'invitation/:id',
        component: InvitationComponent,
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
        canActivate: [userGuard]
    },
    {
        path: '**',
        redirectTo: ''
    }
];
