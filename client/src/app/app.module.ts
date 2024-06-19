import { NgModule } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

//Routes
import { routes } from './app.route';

import { AppComponent } from './app.component';

// service
import { AppService } from './service/app.service';

// store
import { StoreModule } from '@ngrx/store';
import { indexReducer } from './store/index.reducer';

// i18n
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

// headlessui
import { MenuModule } from 'headlessui-angular';

// perfect-scrollbar
import { NgScrollbarModule } from 'ngx-scrollbar';

// dashboard
import { IndexComponent } from './index';

// Layouts
import { AppLayout } from './layouts/app-layout';
import { AuthLayout } from './layouts/auth-layout';

import { HeaderComponent } from './layouts/header';
import { FooterComponent } from './layouts/footer';
import { SidebarComponent } from './layouts/sidebar';
import { ThemeCustomizerComponent } from './layouts/theme-customizer';
import { IconModule } from './shared/icon/icon.module';

@NgModule({
    imports: [
        RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' }),
        BrowserModule,
        BrowserModule,
        BrowserAnimationsModule,
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        MenuModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: httpTranslateLoader,
                deps: [HttpClient],
            },
        }),
        StoreModule.forRoot({ index: indexReducer }),
        NgScrollbarModule.withConfig({
            visibility: 'hover',
            appearance: 'standard',
        }),
        IconModule,
    ],
    declarations: [AppComponent, HeaderComponent, FooterComponent, SidebarComponent, ThemeCustomizerComponent, IndexComponent, AppLayout, AuthLayout],
    providers: [AppService, Title],
    bootstrap: [AppComponent],
})
export class AppModule {}

// AOT compilation support
export function httpTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http);
}
