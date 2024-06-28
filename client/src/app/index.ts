import { Component, OnInit, ViewChild } from '@angular/core';
import { slideDownUp } from './shared/animations';
import { Business } from './models/business';
import { CardType } from './shared/types';
import { Router } from '@angular/router';
import { ModalComponent } from 'angular-custom-modal';
import { BusinessService } from 'src/app/service/business/business.service';
import { lastValueFrom  } from 'rxjs';
import { UserService } from 'src/app/service/user/user.service';
import { User } from 'src/app/models/user';

@Component({
    moduleId: module.id,
    templateUrl: './index.html',
    animations: [slideDownUp],

})
export class IndexComponent implements OnInit {
    userData: User | null = null;
    cardType: typeof CardType = CardType;

    activeTab: any = 'general';
    active1: any = 1;
    active2: any = 1;

    listOwnBusiness: Business[] = [];
    listSharedBusiness: Business[] = [];


    constructor(private businessService: BusinessService,private userService: UserService) {
    }

    ngOnInit(): void {
      this.userService.getUserData().subscribe(userData => {
        this.userData = userData;
      });
      this.getBusiness();
    }

    async getBusiness() {
        try {

            if (!this.userData?.uuid) {
              console.error('No se pudo obtener el UUID del usuario');
              return;
            }

            const businesses: Business[] = await lastValueFrom(this.businessService.getAllBusinesses(this.userData.uuid));
            this.listOwnBusiness = businesses;
            this.listSharedBusiness = businesses;
        } catch (error) {
            console.error('Error fetching businesses', error);
        }
    }
  
  handleBusinessDeleted(id: number) {
      this.listOwnBusiness = this.listOwnBusiness.filter(business => business.uuid !== id);
      this.listSharedBusiness = this.listSharedBusiness.filter(business => business.uuid !== id);
  }
}
