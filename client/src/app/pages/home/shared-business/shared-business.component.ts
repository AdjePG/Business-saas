import { Component, OnInit } from '@angular/core';
import { Business } from '../../../models/business';
import { BusinessElementType, DisplayType } from '../../../shared/types';
import { BusinessService } from 'src/app/service/business/business.service';
import { lastValueFrom  } from 'rxjs';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/service/user/user.service';

@Component({
	selector: 'app-shared-business',
	templateUrl: './shared-business.component.html'
})
export class SharedBusinessComponent implements OnInit {
	cardType: typeof BusinessElementType = BusinessElementType;
	DisplayType = DisplayType;
	displayType: DisplayType = DisplayType.LIST;
	businesses: Business[] = [];
	userData: User | null = null;

	constructor(
		private businessService: BusinessService,
		private userService: UserService) {
	}
  
	ngOnInit(): void {
		this.userService.getUserData().subscribe(userData => this.userData = userData);
		this.getBusiness();
	}
	
	async getBusiness() {
		try {

			if (!this.userData?.uuid) {
				console.error('No se pudo obtener el UUID del usuario');
				return;
			}
			
			this.businesses = (await lastValueFrom(this.businessService.getBusinesses(this.userData.uuid, true))).result;
		} catch (error) {
			console.error('Error fetching businesses', error);
		}
	}

	handleBusinessDeleted(business : { uuid: string }) {
		this.businesses = this.businesses.filter(ownBusiness => ownBusiness.uuid !== business.uuid);
	}
}
