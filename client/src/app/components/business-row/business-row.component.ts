import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalComponent } from 'angular-custom-modal';
import { firstValueFrom } from 'rxjs';
import { Business } from 'src/app/models/business';
import { BusinessInvitation } from 'src/app/models/business-invitation';
import { User } from 'src/app/models/user';
import { BusinessService } from 'src/app/service/business/business.service';
import { InvitationService } from 'src/app/service/invitation/invitation.service';
import { UserService } from 'src/app/service/user/user.service';
import { showAlert } from 'src/app/shared/alerts';
import { BusinessModalType, BusinessElementType, ToastType } from 'src/app/shared/types';

@Component({
  selector: '[business-row]',
  templateUrl: './business-row.component.html'
})
export class BusinessRowComponent implements OnInit {
	@ViewChild('businessModal') businessModal!: ModalComponent;
	@ViewChild('deleteBusinessModal') deleteBusinessModal!: ModalComponent;
	@ViewChild('shareBusinessModal') shareBusinessModal!: ModalComponent;

	@Input() businessElement: BusinessElementType = BusinessElementType.INFO
	@Input() business!: Business;
	@Input() isOwnBusiness: boolean = true;

	@Output() deleteEvent: EventEmitter<{uuid: string, isOwnBusiness: boolean}> = new EventEmitter<{uuid: string, isOwnBusiness: boolean}>();
	@Output() addEvent: EventEmitter<Business> = new EventEmitter<Business>();
	@Output() updateEvent: EventEmitter<Business> = new EventEmitter<Business>();

	businessModalForm: FormGroup;
	shareBusinessForm: FormGroup;

	businessElementType: typeof BusinessElementType = BusinessElementType;
	businessModalType: typeof BusinessModalType = BusinessModalType;

	userData: User | null = null;

	isEditMode: boolean = false;
	isSubmitted: boolean = false

	constructor(
		private formBuilder: FormBuilder, 
		private router: Router,
		private businessService: BusinessService,
		private invitationService: InvitationService,
		private userService: UserService
	) {
		this.businessModalForm = this.formBuilder.group({
			name: ['', Validators.required],
			imagePath: [''],
			description: [''],
			uuid: ['']
		});

		this.shareBusinessForm = this.formBuilder.group({
			email: ['', [Validators.required, Validators.email]]
		}, {updateOn: 'submit'});
	}

	ngOnInit(): void {
		this.userService.getUserData().subscribe(userData => {
			this.userData = userData;
		});
	}

	goToBusiness(uuid: string) {
		this.router.navigate([`/${uuid}`]).then(() => {
			window.location.reload();
		});;
	}

	openModal(event: Event, modal: BusinessModalType, business?: Business) {
		this.isSubmitted = false

		if (modal === BusinessModalType.ADD) {
			this.isEditMode = false;
			this.businessModalForm.reset();
			this.businessModal.open();
		} else if (modal === BusinessModalType.UPDATE && business) {
			this.isEditMode = true;
			this.businessModalForm.setValue({
				name: business.name,
				imagePath: business.imagePath,
				description: business.description,
				uuid: business.uuid
			});
			this.businessModal.open();
		} else if (modal === BusinessModalType.DELETE) {
			this.deleteBusinessModal.open();
		} else if (modal === BusinessModalType.SHARE) {
			this.shareBusinessForm.reset();
			this.shareBusinessModal.open();
		}

		event.stopPropagation();
	}

	closeModal(modal: ModalComponent, form?: FormGroup) {
		modal.close();
		if (form) {
			form.reset();
		}
	}

	saveBusinessData() {
		this.isSubmitted = true

		if (this.businessModalForm.invalid) {
			return;
		}

		if (!this.userData?.uuid) {
			console.error('No se pudo obtener el UUID del usuario');
			return;
		}

		let businessData: Business = {
			...this.businessModalForm.value,
			uuidUser: this.userData.uuid
		};

		try {
			if (this.isEditMode) {
				this.updateBusiness(businessData)
			} else {
				this.createBusiness(businessData);
			}

			this.closeModal(this.businessModal, this.businessModalForm);
		} catch (error) {
			showAlert({
				toastType: ToastType.ERROR,
				message: (error as Error).message
			});
		}
	}

	async createBusiness(businessData: Business) {
		businessData = await this.checkImage(businessData);

		const response = await firstValueFrom(this.businessService.createBusiness(businessData))

		if (response.status !== 201) {
			throw new Error(response.message);
		}

		showAlert({
			toastType: ToastType.SUCCESS,
			message: 'Negocio creado con éxito'
		});

		response.result = await this.checkImage(response.result);
		this.addEvent.emit(response.result);
	}

	async updateBusiness(businessData: Business) {
		businessData = await this.checkImage(businessData);
		
		const response = await firstValueFrom(this.businessService.updateBusiness(businessData))

		if (response.status !== 200) {
			throw new Error(response.message);
		}

		showAlert({
			toastType: ToastType.SUCCESS,
			message: 'Negocio actualizado con éxito'
		});

		response.result = await this.checkImage(response.result);
		this.updateEvent.emit(response.result);
	}

	async deleteBusiness() {
		try {
			let response

			if (this.isOwnBusiness) {
				response = await firstValueFrom(this.businessService.deleteBusiness(this.business.uuid))
			} else {
				response = await firstValueFrom(this.businessService.deleteBusiness(this.business.uuid, true))
			}

			if (response.status !== 200) {
				throw new Error(response.message);
			}

			showAlert({
				toastType: ToastType.SUCCESS,
				message: 'Negocio eliminado con éxito'
			});

			this.deleteEvent.emit({uuid: this.business.uuid, isOwnBusiness: this.isOwnBusiness});
			this.closeModal(this.deleteBusinessModal);
		} catch (error) {
			showAlert({
				toastType: ToastType.ERROR,
				message: (error as Error).message
			});
		}
	}

	async shareBusiness() {
		this.isSubmitted = true

		if (this.shareBusinessForm.invalid) {
			return;
		}
		
		this.isSubmitted = false
		
		const businessInvitation: BusinessInvitation = {
			...this.shareBusinessForm.value,
		};

		try {
			const response = await firstValueFrom(this.invitationService.shareBusiness(this.business.uuid, businessInvitation))

			if (response.status !== 200) {
				throw new Error(response.message);
			}

			showAlert({
				toastType: ToastType.INFO,
				message: 'Se ha enviado una invitación'
			});

			this.closeModal(this.shareBusinessModal, this.shareBusinessForm);
		} catch (error) {
			showAlert({
				toastType: ToastType.ERROR,
				message: (error as Error).message
			});
		}
	}

	checkImage(business: Business): Promise<Business> {
		return new Promise((resolve) => {
			const img = new Image();
			img.src = business.imagePath;
			img.onerror = () => {
				business.imagePath = '';
				resolve(business);
			};
			img.onload = () => {
				resolve(business);
			};
		})
	}
}
