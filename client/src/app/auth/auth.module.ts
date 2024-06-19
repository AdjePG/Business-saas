import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';

// icon
import { IconModule } from 'src/app/shared/icon/icon.module';

import { CoverLockscreenComponent } from './cover-lockscreen';
import { CoverLoginComponent } from './cover-login';
import { CoverPasswordResetComponent } from './cover-password-reset';
import { CoverRegisterComponent } from './cover-register';

// headlessui
import { MenuModule } from 'headlessui-angular';

const routes: Routes = [
    { path: 'lockscreen', component: CoverLockscreenComponent, title: 'Lockscreen' },
    { path: 'login', component: CoverLoginComponent, title: 'Log In' },
    { path: 'reset-password', component: CoverPasswordResetComponent, title: 'Password Reset' },
    { path: 'signup', component: CoverRegisterComponent, title: 'Sign Up' },
];

@NgModule({
    imports: [RouterModule.forChild(routes), CommonModule, MenuModule, IconModule],
    declarations: [
        CoverLockscreenComponent,
        CoverLoginComponent,
        CoverPasswordResetComponent,
        CoverRegisterComponent,
    ],
})

export class AuthModule { }
