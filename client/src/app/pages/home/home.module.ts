import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { OwnBusinessComponent as OwnBusinessesComponent } from './own-business/own-business.component';
import { SharedBusinessComponent as SharedBusinessesComponent } from './shared-business/shared-business.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IconModule } from 'src/app/shared/icon/icon.module';
import { ModalModule } from 'angular-custom-modal';
import { HomeComponent } from './home/home.component';
import { BusinessCardComponent } from 'src/app/components/business-card/business-card.component';
import { BusinessRowComponent } from 'src/app/components/business-row/business-row.component';
import { SharedComponentsModule } from 'src/app/shared/shared-components.module';

const routes: Routes = [
  { 
    path: '', 
    component: HomeComponent, 
    title: 'Inicio' 
  },
  { 
    path: 'own-businesses', 
    component: OwnBusinessesComponent, 
    title: 'Mis negocios' 
  },
  { 
    path: 'shared-businesses', 
    component: SharedBusinessesComponent, 
    title: 'Negocios compartidos' 
  }
];

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    FormsModule,
    IconModule,
    SharedComponentsModule,
    ModalModule
  ],
  declarations: [
    OwnBusinessesComponent,
    SharedBusinessesComponent,
    HomeComponent,
    BusinessCardComponent,
    BusinessRowComponent
  ]
})
export class HomeModule { }
