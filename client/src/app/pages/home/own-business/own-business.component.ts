import { Component, OnInit, ViewChild } from '@angular/core';
import { slideDownUp } from '../../../shared/animations';
import { Business } from '../../../models/business';
import { CardType } from '../../../shared/types';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalComponent } from 'angular-custom-modal';
import { BusinessService } from 'src/app/service/business/business.service';
import { lastValueFrom,firstValueFrom  } from 'rxjs';
import { getUuidUserFromToken } from 'src/app/shared/token-utils';
@Component({
  selector: 'app-own-business',
  templateUrl: './own-business.component.html'
})
export class OwnBusinessComponent implements OnInit {
  @ViewChild('addBusinessModal') addBusinessModal!: ModalComponent;
  cardType: typeof CardType = CardType;

  activeTab: any = 'general';
  active1: any = 1;
  active2: any = 1;

  listOwnBusiness: Business[] = [];
  listSharedBusiness: Business[] = [];

  businessModalForm: FormGroup;


  constructor(private formBuilder: FormBuilder, private router: Router,private businessService: BusinessService) {
      this.businessModalForm = this.formBuilder.group({
          name: ['', Validators.required],
          imagePath: ['', Validators.required],
          description: ['']

      });
  }

  ngOnInit() {
      this.getBusiness();
  }

  async createBusiness() {
      if (this.businessModalForm.invalid) {
        return;
      }
  
      const token = localStorage.getItem('user-auth');
      const uuidUser = token ? getUuidUserFromToken(token) : null;
  
      console.log(uuidUser);
      if (!uuidUser) {
        console.error('No se pudo obtener el UUID del usuario');
        return;
      }
  
      const business: Business = {
        ...this.businessModalForm.value,
        uuidUser: uuidUser // Añadir uuidUser al objeto Business
      };
  
      try {
        const createdBusiness = await firstValueFrom(this.businessService.createBusiness(business));
        if (createdBusiness) {
          this.listOwnBusiness.push(createdBusiness); // Actualiza la lista de negocios
        }
        this.closeModal();
      } catch (error) {
        console.error('Error creating business', error);
      }
    }
  
  async getBusiness() {
      try {
          const businesses: Business[] = await lastValueFrom(this.businessService.getAllBusinesses(3));
          this.listOwnBusiness = businesses;
          this.listSharedBusiness = businesses;
      } catch (error) {
          console.error('Error fetching businesses', error);
      }
  }

  goToBusiness(id: number) {
      this.router.navigate([`/business/${id}`]);
  }
  
  closeModal() {
      this.businessModalForm.reset();
      this.addBusinessModal.close();
  }

  handleBusinessDeleted(id: number) {
      this.listOwnBusiness = this.listOwnBusiness.filter(business => business.uuid !== id);
      this.listSharedBusiness = this.listSharedBusiness.filter(business => business.uuid !== id);
  }
}
