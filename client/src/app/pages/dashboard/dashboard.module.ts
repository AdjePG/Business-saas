import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { IconModule } from 'src/app/shared/icon/icon.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ModalModule } from 'angular-custom-modal';
import { DashboardComponent } from './dashboard.component';
import { InvoicesComponent } from './invoices/invoices.component';
import { CustomersComponent } from './customers/customers.component';
import { ItemsComponent } from './items/items.component';

const routes: Routes = [
  { 
    path: '', 
    component: DashboardComponent, 
    title: 'NOMBRE EMPRESA | Panel principal' 
  },
  { 
    path: 'items', 
    component: ItemsComponent, 
    title: 'NOMBRE EMPRESA | Artículos y servicios' 
  },
  { 
    path: 'customers', 
    component: CustomersComponent, 
    title: 'NOMBRE EMPRESA | Clientes' 
  },
  { 
    path: 'invoices', 
    component: InvoicesComponent, 
    title: 'NOMBRE EMPRESA | Facturas' 
  },
  {
      path: '**',
      redirectTo: ''
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
    CustomersComponent,
    DashboardComponent,
    InvoicesComponent,
    ItemsComponent
  ]
})
export class DashboardModule { }
