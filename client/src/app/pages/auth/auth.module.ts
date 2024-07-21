import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';


// icon
import { IconModule } from 'src/app/shared/icon/icon.module';

import { CoverLockscreenComponent } from './lockscreen/cover-lockscreen';
import { CoverLoginComponent } from './login/cover-login';
import { CoverPasswordResetComponent } from './password-reset/cover-password-reset';
import { CoverRegisterComponent } from './register/cover-register';

// headlessui
import { MenuModule } from 'headlessui-angular';

// i18n
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';


const routes: Routes = [
    { 
        path: 'lockscreen', 
        component: CoverLockscreenComponent, title: 
        'Lockscreen' 
    },
    { 
        path: 'login', 
        component: CoverLoginComponent, 
        title: 'Log In' 
    },
    { 
        path: 'reset-password', 
        component: CoverPasswordResetComponent, 
        title: 'Password Reset' 
    },
    { 
        path: 'signup', 
        component: CoverRegisterComponent, 
        title: 'Sign Up' 
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(routes), 
        CommonModule, 
        MenuModule, 
        IconModule, 
        FormsModule,
        ReactiveFormsModule,
        TranslateModule.forChild({
            loader: {
                provide: TranslateLoader,
                useFactory: httpTranslateLoader,
                deps: [HttpClient],
            },
        }),

    ],
    declarations: [
        CoverLockscreenComponent,
        CoverLoginComponent,
        CoverPasswordResetComponent,
        CoverRegisterComponent,
    ],
})

export class AuthModule { }

// AOT compilation support
export function httpTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http);
}
