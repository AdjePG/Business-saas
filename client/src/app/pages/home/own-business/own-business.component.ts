import { Component, OnInit } from '@angular/core';
import { Business } from '../../../models/business';
import { BusinessElementType, DisplayType } from '../../../shared/types';
import { BusinessService } from 'src/app/service/business/business.service';
import { lastValueFrom  } from 'rxjs';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/service/user/user.service';
import { Router } from '@angular/router';

@Component({
	selector: 'app-own-business',
	templateUrl: './own-business.component.html'
})
export class OwnBusinessComponent implements OnInit {
	businessElementType: typeof BusinessElementType = BusinessElementType;
	displayType: typeof DisplayType = DisplayType;
	display: DisplayType = DisplayType.LIST;
	businesses: Business[] = [];
	userData: User | null = null;

	constructor(
		private router: Router,
		private businessService: BusinessService,
		private userService: UserService
	) {

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

			const response = await lastValueFrom(this.businessService.getBusinesses(this.userData.uuid))

			if (response.status !== 200) {
				throw new Error(response.message)
			}

			this.businesses = response.result;
		} catch (error) {
			console.error('Error fetching businesses:', error);
		}
  	}

	handleBusinessAdded(business: Business) {
		this.businesses.push(business);
	}

	handleBusinessUpdated(business : Business) {
		let index;

		index = this.businesses.findIndex(ownBusiness => ownBusiness.uuid === business.uuid);

		if (index !== -1) {
			this.businesses[index] = business
		}
	}
	
	handleBusinessDeleted(business : { uuid: string }) {
		this.businesses = this.businesses.filter(ownBusiness => ownBusiness.uuid !== business.uuid);
	}

	goToBusiness(uuid: string) {
		this.router.navigate([`/${uuid}`]).then(() => {
			window.location.reload();
		});;
	}
}
