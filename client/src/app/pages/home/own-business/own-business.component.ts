import { Component, OnInit } from '@angular/core';
import { Business } from '../../../models/business';
import { CardType } from '../../../shared/types';
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
  userData: User | null = null;

  constructor(
	private businessService: BusinessService,private userService: UserService) {
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

			const response = await lastValueFrom(this.businessService.getBusinesses(this.userData.uuid))

			if (response.status !== 200) {
				throw new Error(response.message)
			}

			this.listOwnBusiness = response.result;
		} catch (error) {
			console.error('Error fetching businesses:', error);
		}
  	}

  handleBusinessDeleted(id: string) {
      this.listOwnBusiness = this.listOwnBusiness.filter(business => business.uuid !== id);
  }

  handleBusinessAdded(business: Business) {
    this.listOwnBusiness.push(business);
  }

  handleBusinessUpdate(updatedBusiness: Business) {
    console.log(updatedBusiness);
    console.log(this.listOwnBusiness);

    const index = this.listOwnBusiness.findIndex(b => b.uuid === updatedBusiness.uuid);
    console.log(index);

    if (index !== -1) {
      this.listOwnBusiness[index] = updatedBusiness;
    }
  }

 
}
