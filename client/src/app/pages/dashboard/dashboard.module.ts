import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { CustomersComponent } from './customers/customers';
import { IconModule } from 'src/app/shared/icon/icon.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ModalModule } from 'angular-custom-modal';

const routes: Routes = [
  { 
    path: '', 
    component: CustomersComponent, 
    title: 'Clientes' 
  },
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
    CustomersComponent
  ]
})
export class DashboardModule { }
