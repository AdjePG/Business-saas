import { Component, OnInit } from '@angular/core';
import { slideDownUp } from './shared/animations';
import { Business } from './models/business';
import { CardType } from './shared/types';
import { BusinessService } from 'src/app/service/business/business.service';
import { UserService } from 'src/app/service/user/user.service';
import { User } from 'src/app/models/user';
import { lastValueFrom } from 'rxjs';

@Component({
    templateUrl: './index.html',
    animations: [slideDownUp],
})
export class IndexComponent implements OnInit {
    cardType: typeof CardType = CardType;
	
    listOwnBusinesses: Business[] = [];
    listSharedBusinesses: Business[] = [];
    userData: User | null = null;

    activeTab: any = 'general';
    active1: any = 1;
    active2: any = 1;

    constructor(
		private businessService: BusinessService,
		private userService: UserService
	) {

    }

    ngOnInit(): void {
		this.userService.getUserData().subscribe(userData => {
			this.userData = userData;
		});

		this.getBusiness();
    }

    async getBusiness() {
		let response;

        try {
            if (!this.userData?.uuid) {
				console.error('No se pudo obtener el UUID del usuario');
				return;
            }

			response = await lastValueFrom(this.businessService.getBusinesses(this.userData.uuid))

			if (response.status !== 200) {
				console.error(response.message);
				return;
			}

            this.listOwnBusinesses = response.result;

			response = await lastValueFrom(this.businessService.getBusinesses(this.userData.uuid, true))

			if (response.status !== 200) {
				console.error(response.message);
				return;
			}

            this.listSharedBusinesses = response.result;
        } catch (error) {
            console.error('Error fetching businesses', error);
        }
    }
	
	handleBusinessDeleted(uuid: string) {
		this.listOwnBusinesses = this.listOwnBusinesses.filter(business => business.uuid !== uuid);
	}

	handleBusinessAdded(business: Business) {
		this.listOwnBusinesses.push(business);
	}
}
