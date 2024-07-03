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


@Component({
  selector: 'card',
  templateUrl: './business-card.component.html'
})
export class BusinessCardComponent implements OnInit {
  @ViewChild('addBusinessModal') addBusinessModal!: ModalComponent;
  @ViewChild('deleteBusinessModal') deleteBusinessModal!: ModalComponent;

  @Input() cardType: CardType = CardType.INFO
  @Input() business!: Business;
  @Input() isOwnBusiness: boolean = false;

  @Output() deleteEvent: EventEmitter<string> = new EventEmitter<string>();
  @Output() addEvent: EventEmitter<Business> = new EventEmitter<Business>();

  cardModalType: typeof CardModalType = CardModalType;
  userData: User | null = null;
  listOwnBusiness: Business[] = [];
  listSharedBusiness: Business[] = [];
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
        description: ['']
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

  openModal(modal : CardModalType, business? : Business) {
    if (modal = CardModalType.ADD) {
      this.businessModalForm = this.formBuilder.group({
        name: ['', Validators.required],
        imagePath: [''],
        description: ['']
      });
    } else if (modal = CardModalType.UPDATE) {
      this.businessModalForm = this.formBuilder.group({
        name: [business?.name, Validators.required],
        imagePath: [business?.imagePath],
        description: [business?.description]
      });
    }
  }

  closeModal() {
    this.businessModalForm.reset();
    this.addBusinessModal.close();
  }

  async createBusiness() {
    if (this.businessModalForm.invalid) {
      return;
    }

    if (!this.userData?.uuid) {
      console.error('No se pudo obtener el UUID del usuario');
      return;
    }

    const business: Business = {
      ...this.businessModalForm.value,
      uuidUser: this.userData.uuid
    };

    try {
      let createdBusiness = (await firstValueFrom(this.businessService.createBusiness(business))).result;

      if (createdBusiness) {
        createdBusiness = await this.checkImage(createdBusiness);
        this.addEvent.emit(createdBusiness);
        this.closeModal();
      }
    } catch (error) {
      console.error('Error creating business', error);
    }
  }

  async updateBusiness() {
    if (this.businessModalForm.invalid) {
      return;
    }

    if (!this.userData?.uuid) {
      console.error('No se pudo obtener el UUID del usuario');
      return;
    }

    const business: Business = {
      ...this.businessModalForm.value,
      uuidUser: this.userData.uuid
    };

    try {
      let createdBusiness = (await firstValueFrom(this.businessService.createBusiness(business))).result;

      if (createdBusiness) {
        createdBusiness = await this.checkImage(createdBusiness);
        this.addEvent.emit(createdBusiness);
        this.closeModal();
      }
    } catch (error) {
      console.error('Error creating business', error);
    }
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
    console.log("AAAA")
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
