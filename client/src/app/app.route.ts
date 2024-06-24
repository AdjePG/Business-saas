import { Routes } from '@angular/router';
import { IndexComponent } from './index';
import { AppLayout } from './layouts/app-layout';
import { AuthLayout } from './layouts/auth-layout';
import { authGuard } from './shared/auth-guard';

export const routes: Routes = [
    {
        path: '',
        component: AppLayout,
        children: [
            { path: '', component: IndexComponent, title: 'Inicio' },
        ],
        canActivate: [authGuard]
    },
    {
        path: '',
        component: AuthLayout,
        children: [
            { path: 'auth', loadChildren: () => import('./auth/auth.module').then((d) => d.AuthModule) },
        ],
        canActivate: [authGuard]
    },
    {
        path: '**',
        redirectTo: ''
    }
];
