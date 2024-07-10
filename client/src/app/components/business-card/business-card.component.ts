import { Component, EventEmitter, Input, Output,ViewChild, OnInit } from '@angular/core';
import { CardModalType, CardType } from 'src/app/shared/types';
import { BusinessService } from 'src/app/service/business/business.service';
import { Business } from '../../models/business';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalComponent } from 'angular-custom-modal';
import { Observable, firstValueFrom  } from 'rxjs';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/service/user/user.service';
import { showAlert } from 'src/app/shared/alerts';
import { ToastType } from 'src/app/shared/types';

@Component({
  selector: 'card',
  templateUrl: './business-card.component.html'
})
export class BusinessCardComponent implements OnInit {
  @ViewChild('businessModal') businessModal!: ModalComponent;

  @ViewChild('deleteBusinessModal') deleteBusinessModal!: ModalComponent;

  @Input() cardType: CardType = CardType.INFO
  @Input() business!: Business;
  @Input() isOwnBusiness: boolean = false;

  @Output() deleteEvent: EventEmitter<string> = new EventEmitter<string>();
  @Output() addEvent: EventEmitter<Business> = new EventEmitter<Business>();
  @Output() updateEvent: EventEmitter<Business> = new EventEmitter<Business>();

  cardModalType: typeof CardModalType = CardModalType;
  isEditMode: boolean = false;

  userData: User | null = null;
  businessModalForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder, 
    private router: Router,
    private businessService: BusinessService,
    private userService: UserService
  ) {
    this.businessModalForm = this.formBuilder.group({
        name: ['', Validators.required],
        imagePath: [''],
        description: [''],
        uuid: ['']
    });
  }

  ngOnInit(): void {
    this.userService.getUserData().subscribe(userData => {
      this.userData = userData;
    });
  }

  getType() {
    return this.cardType === CardType.INFO
  }

  openModal(modal: CardModalType, business?: Business) {
    if (modal === CardModalType.ADD) {
      this.isEditMode = false;
      this.businessModalForm.reset();
      this.businessModal.open();
    } else if (modal === CardModalType.UPDATE && business) {
      this.isEditMode = true;
      this.businessModalForm.setValue({
        name: business.name,
        imagePath: business.imagePath,
        description: business.description,
        uuid: business.uuid
      });
      this.businessModal.open();
    }
  }

  closeModal() {
    this.businessModal.close();
    this.businessModalForm.reset();
  }

  async saveBusiness() {
    if (this.businessModalForm.invalid) {
      return;
    }

    if (!this.userData?.uuid) {
      console.error('No se pudo obtener el UUID del usuario');
      return;
    }

    const businessData: Business = {
      ...this.businessModalForm.value,
      uuidUser: this.userData.uuid
    };

    try {
      let savedBusiness;
      if (this.isEditMode) {
        savedBusiness = await this.updateBusiness(businessData);
        showAlert({
          toastType: ToastType.SUCCESS,
          message: 'Negocio actualizado con éxito'
        });
      } else {
        savedBusiness = await this.createBusiness(businessData);
        showAlert({
          toastType: ToastType.SUCCESS,
          message: 'Negocio creado con éxito'
        });
      }

      if (savedBusiness) {
        savedBusiness = await this.checkImage(savedBusiness);
        if (this.isEditMode) {
          this.updateEvent.emit(savedBusiness);
        } else {
          this.addEvent.emit(savedBusiness);
        }
        this.closeModal();
      }
    } catch (error) {
      console.error('Error saving business', error);
      showAlert({
        toastType: ToastType.ERROR,
        message: 'error'
      });
    }
  }

  async createBusiness(business: Business): Promise<Business> {
    return (await firstValueFrom(this.businessService.createBusiness(business))).result;
  }


  async updateBusiness(business: Business): Promise<Business> {
    console.log('updateBusiness'+business.uuid);
    return (await firstValueFrom(this.businessService.updateBusiness(business.uuid,business))).result;
  }

  async deleteBusiness() {
    try {
      await firstValueFrom(this.businessService.deleteBusiness(this.business.uuid));
      this.deleteEvent.emit(this.business.uuid);
      this.deleteBusinessModal.close();
    } catch (error) {
      console.error('Error deleting business', error);
    }
  }

  goToBusiness(id: string) {
    this.router.navigate([`/${id}`]);
  }

  checkImage(business: Business) {
    const img = new Image();
    img.src = business.imagePath;
    img.onerror = () => {
      business.imagePath = '';
    };

    return business
  }
}
