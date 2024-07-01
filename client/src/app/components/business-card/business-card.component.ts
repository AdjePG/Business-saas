import { Component, EventEmitter, Input, Output,ViewChild, OnInit } from '@angular/core';
import { CardType } from 'src/app/shared/types';
import { BusinessService } from 'src/app/service/business/business.service';
import { Business } from '../../models/business';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalComponent } from 'angular-custom-modal';
import { firstValueFrom  } from 'rxjs';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/service/user/user.service';


@Component({
  selector: 'card',
  templateUrl: './business-card.component.html'
})
export class BusinessCardComponent implements OnInit {
  @ViewChild('addBusinessModal') addBusinessModal!: ModalComponent;

  @Input() cardType: CardType = CardType.INFO
  @Input() business!: Business;
  @Input() isOwnBusiness: boolean = false;

  @Output() deleteEvent: EventEmitter<number> = new EventEmitter<number>();
  @Output() addEvent: EventEmitter<Business> = new EventEmitter<Business>();

  userData: User | null = null;
  listOwnBusiness: Business[] = [];
  listSharedBusiness: Business[] = [];
  businessModalForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private router: Router,private businessService: BusinessService,private userService: UserService) {
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
      uuidUser: this.userData.uuid // Añadir uuidUser al objeto Business
    };

    try {
      const createdBusiness = await firstValueFrom(this.businessService.createBusiness(business));
      if (createdBusiness) {
        this.listOwnBusiness.push(createdBusiness);
        //this.addEvent.emit(createdBusiness);
        this.closeModal();
      }
    } catch (error) {
      console.error('Error creating business', error);
    }
  }

  
  goToBusiness(id: number) {
      this.router.navigate([`/business/${id}`]);
  }

  async deleteBusiness() {
    try {
      await firstValueFrom(this.businessService.deleteBusiness(this.business.uuid));
      this.deleteEvent.emit(this.business.uuid);
    } catch (error) {
      console.error('Error deleting business', error);
    }
  }

  closeModal() {
    this.businessModalForm.reset();
    this.addBusinessModal.close();
  }
}
