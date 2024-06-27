import { Component, OnInit, ViewChild } from '@angular/core';
import { slideDownUp } from './shared/animations';
import { Business } from './models/business';
import { CardType } from './shared/types';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalComponent } from 'angular-custom-modal';

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

    mockResponse: any = {
        "status": 200,
        "message": "Success!",
        "result": [
            {
                "id": 1,
                "name": "Negocio 1",
                "photo": "/assets/images/knowledge/image-1.jpg"
            },
            {
                "id": 2,
                "name": "Negocio 2",
                "photo": "/assets/images/knowledge/image-8.jpg"
            },
            {
                "id": 3,
                "name": "Negocio 3",
                "photo": ""
            }
        ]
    }

    constructor(private formBuilder: FormBuilder, private router: Router) {
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
        this.listOwnBusiness = this.mockResponse.result
        this.listSharedBusiness = this.mockResponse.result
    }

    goToBusiness(id: number) {
        //this.router.navigate([``])
    }
}
