import { Component, OnInit, ViewChild } from '@angular/core';
import { slideDownUp } from './shared/animations';
import { Business } from './models/business';
import { CardType } from './shared/types';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalComponent } from 'angular-custom-modal';
import { BusinessService } from 'src/app/service/business/business.service';

@Component({
    moduleId: module.id,
    templateUrl: './index.html',
    animations: [slideDownUp],

})
export class IndexComponent implements OnInit {
    @ViewChild('addBusinessModal') addBusinessModal!: ModalComponent;
    cardType: typeof CardType = CardType;

    activeTab: any = 'general';
    active1: any = 1;
    active2: any = 1;

    listOwnBusiness: Business[] = [];
    listSharedBusiness: Business[] = [];

    businessModalForm: FormGroup;


    constructor(private formBuilder: FormBuilder, private router: Router,private businessService: BusinessService) {
        this.businessModalForm = this.formBuilder.group({
            name: ['', Validators.required]
        });
    }

    ngOnInit() {
        this.getBusiness();
    }

    createBusiness() {
        if (this.businessModalForm.invalid) {
            return;
        }

        this.addBusinessModal.close()
    }

    getBusiness() {
        this.businessService.getAllBusinesses(3).subscribe(
          (businesses: Business[]) => {
            this.listOwnBusiness = businesses;
            this.listSharedBusiness = businesses;
          },
          (error) => {
            console.error('Error fetching businesses', error);
          }
        );
      }

    goToBusiness(id: number) {
        //this.router.navigate([``])
    }
}
