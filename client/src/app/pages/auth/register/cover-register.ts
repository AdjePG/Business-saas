import { animate, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateService } from '@ngx-translate/core';
import { AppService } from 'src/app/service/app.service';
import { AuthService } from 'src/app/service/auth/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { showAlert } from 'src/app/shared/alerts';
import { ToastType } from 'src/app/shared/types';
import { User } from 'src/app/models/user';

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
    signUpForm: FormGroup;

    store: any;
    currYear: number = new Date().getFullYear();

    passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";  // 8 Caracteer , 1 Mayuscula 1 Simbolo 1 Numero

    constructor(
		private formBuilder: FormBuilder, 
        private authService: AuthService,
        public translate: TranslateService, 
        public storeData: Store<any>, 
        public router: Router, 
        private appSetting: AppService
    ) {
        this.initStore();
        this.signUpForm = this.formBuilder.group({
			name: ['', Validators.required],
			email: ['', [Validators.required, Validators.email]],
			password: ['', [Validators.required, Validators.pattern(this.passwordPattern), Validators.minLength(8)]]
		});
    }

    signUp() {
        if (this.signUpForm.invalid) {
            return;  // No hacer nada si el formulario es inválido
        }

        const userData : User = {
			...this.signUpForm.value
        };

        this.authService.signUp(userData).subscribe({
            next: () => {
                this.router.navigate(['/']);
            },
            error: (error) => {
                const errorMessage = this.translate.instant('auth.signUp.error.'+error.error.message);

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
