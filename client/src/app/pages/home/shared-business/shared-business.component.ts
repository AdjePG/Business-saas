import { Component, OnInit } from '@angular/core';
import { Business } from '../../../models/business';
import { CardType } from '../../../shared/types';
import { Router } from '@angular/router';
import { ModalComponent } from 'angular-custom-modal';
import { BusinessService } from 'src/app/service/business/business.service';
import { lastValueFrom  } from 'rxjs';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-shared-business',
  templateUrl: './shared-business.component.html'
})
export class SharedBusinessComponent implements OnInit {
  userData: User | null = null;
  cardType: typeof CardType = CardType;
  listSharedBusiness: Business[] = [];

  constructor(private router: Router,private businessService: BusinessService,private userService: UserService) {
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
          this.listSharedBusiness = businesses;
      } catch (error) {
          console.error('Error fetching businesses', error);
      }
  }
}
