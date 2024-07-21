import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PaginationComponent } from '../components/pagination/pagination.component';
import { BreadcrumbsComponent } from '../components/breadcrumbs/breadcrumbs.component';
import { FaqsComponent } from '../components/faqs/faqs.component';
import { IconModule } from './icon/icon.module';

@NgModule({
  imports: [
    CommonModule,
    IconModule
  ],
  declarations: [
    BreadcrumbsComponent,
    PaginationComponent,
    FaqsComponent
  ],
  exports: [
    BreadcrumbsComponent,
    PaginationComponent,
    FaqsComponent
  ]
})
export class SharedComponentsModule { }
