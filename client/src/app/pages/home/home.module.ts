import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { OwnBusinessComponent } from './own-business/own-business.component';
import { SharedBusinessComponent } from './shared-business/shared-business.component';
import { IndexComponent } from 'src/app';

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
    RouterModule.forChild(routes)
  ],
  declarations: []
})
export class HomeModule { }
