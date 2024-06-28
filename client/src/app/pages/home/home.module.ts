import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { OwnBusinessComponent } from './own-business/own-business.component';
import { SharedBusinessComponent } from './shared-business/shared-business.component';
import { IndexComponent } from 'src/app';
import { BusinessCardComponent } from '../../components/business-card/business-card.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IconModule } from 'src/app/shared/icon/icon.module';
import { ModalModule } from 'angular-custom-modal';

const routes: Routes = [
  { 
    path: '', 
    component: IndexComponent, 
    title: 'Inicio' 
  },
  { 
    path: 'own-business', 
    component: OwnBusinessComponent, 
    title: 'Mis negocios' 
  },
  { 
    path: 'shared-business', 
    component: SharedBusinessComponent, 
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
    ModalModule
  ],
  declarations: [
    IndexComponent,
    OwnBusinessComponent,
    SharedBusinessComponent,
    BusinessCardComponent
  ]
})
export class HomeModule { }
