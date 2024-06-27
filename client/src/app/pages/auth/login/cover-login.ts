import { animate, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateService } from '@ngx-translate/core';
import { AppService } from 'src/app/service/app.service';
import { AuthService } from 'src/app/service/auth/auth.service';
import { showAlert } from 'src/app/shared/alerts';
import { ToastType } from 'src/app/shared/types';

@Component({
    moduleId: module.id,
    templateUrl: './cover-login.html',
    animations: [
        trigger('toggleAnimation', [
            transition(':enter', [style({ opacity: 0, transform: 'scale(0.95)' }), animate('100ms ease-out', style({ opacity: 1, transform: 'scale(1)' }))]),
            transition(':leave', [animate('75ms', style({ opacity: 0, transform: 'scale(0.95)' }))]),
        ]),
    ],
})
export class CoverLoginComponent {
    email: string = '';
    password: string = '';
    store: any;
    currYear: number = new Date().getFullYear();

    constructor(private authService: AuthService, public translate: TranslateService, public storeData: Store<any>, public router: Router, private appSetting: AppService) {
        this.initStore();
    }

    async initStore() {
        this.storeData
            .select((d) => d.index)
            .subscribe((d) => {
                this.store = d;
            });
    }

    changeLanguage(item: any) {
        this.translate.use(item.code);
        this.appSetting.toggleLanguage(item);
        if (this.store.locale?.toLowerCase() === 'ae') {
            this.storeData.dispatch({ type: 'toggleRTL', payload: 'rtl' });
        } else {
            this.storeData.dispatch({ type: 'toggleRTL', payload: 'ltr' });
        }
        window.location.reload();
    }

    signIn(form: NgForm) {
        if (form.invalid) {
            return;
        }

        const userData = {
            email: this.email,
            password: this.password
        };

        this.authService.logIn(userData).subscribe({
            next: (response) => {
                localStorage.setItem("user-auth", response.result)
                this.router.navigate(['/']);
            },
            error: (error) => {
                showAlert({
                    toastType: ToastType.ERROR,
                    message: error.error.message
                });
            }
        });
    }
}
