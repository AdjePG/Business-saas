import { animate, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateService } from '@ngx-translate/core';
import { AppService } from 'src/app/service/app.service';
import { AuthService } from 'src/app/service/auth/auth.service';
import { NgForm } from '@angular/forms';
import { showAlert } from 'src/app/shared/alerts';
import { ToastType } from 'src/app/shared/types';

@Component({
    moduleId: module.id,
    templateUrl: './cover-register.html',
    animations: [
        trigger('toggleAnimation', [
            transition(':enter', [style({ opacity: 0, transform: 'scale(0.95)' }), animate('100ms ease-out', style({ opacity: 1, transform: 'scale(1)' }))]),
            transition(':leave', [animate('75ms', style({ opacity: 0, transform: 'scale(0.95)' }))]),
        ]),
    ],
})
export class CoverRegisterComponent {
    name: string = '';
    email: string = '';
    password: string = '';
    isLoading = false; 
    store: any;
    currYear: number = new Date().getFullYear();

    passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";  // 8 Caracteer , 1 Mayuscula 1 Simbolo 1 Numero


    constructor(private authService: AuthService,public translate: TranslateService, public storeData: Store<any>, public router: Router, private appSetting: AppService) {
        this.initStore();
    }

    signUp(form: NgForm) {
        if (form.invalid) {
            return;  // No hacer nada si el formulario es inválido
        }

        this.isLoading = true;  // Iniciar la carga
        const userData = {
            name: this.name,
            email: this.email,
            password: this.password
        };

        this.authService.signUp(userData).subscribe({
            next: () => {
                this.isLoading = false;
                this.router.navigate(['/']);
            },
            error: (error) => {
                this.isLoading = false;
                const errorMessage = this.translate.instant('auth.signUp.error.'+error.error.message);

                console.log(error.error.message);
                console.log(errorMessage);

                showAlert({
                    toastType: ToastType.ERROR,
                    message: errorMessage
                });
            }
        });
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
}
