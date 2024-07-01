import { Component, OnInit } from '@angular/core';
import { Business } from '../../../models/business';
import { CardType } from '../../../shared/types';
import { FormBuilder} from '@angular/forms';
import { Router } from '@angular/router';
import { BusinessService } from 'src/app/service/business/business.service';
import { lastValueFrom  } from 'rxjs';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-own-business',
  templateUrl: './own-business.component.html'
})
export class OwnBusinessComponent implements OnInit {
  cardType: typeof CardType = CardType;


  listOwnBusiness: Business[] = [];
  listSharedBusiness: Business[] = [];

  userData: User | null = null;

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
          this.listOwnBusiness = businesses;
          this.listSharedBusiness = businesses;
      } catch (error) {
          console.error('Error fetching businesses', error);
      }
  }

  handleBusinessDeleted(id: string) {
      this.listOwnBusiness = this.listOwnBusiness.filter(business => business.uuid !== id);
      this.listSharedBusiness = this.listSharedBusiness.filter(business => business.uuid !== id);
  }

 
}
