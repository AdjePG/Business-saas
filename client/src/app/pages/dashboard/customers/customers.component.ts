import { Component, ViewChild } from '@angular/core';
import Swal from 'sweetalert2';
import { animate, style, transition, trigger } from '@angular/animations';
import { ModalComponent } from 'angular-custom-modal';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { lastValueFrom  } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/service/user/user.service';
import { User } from 'src/app/models/user';
import { CustomerService } from 'src/app/service/customer/customer.service';
import { Customer } from 'src/app/models/customer';
@Component({
    templateUrl: './customers.component.html',
    animations: [
        trigger('toggleAnimation', [
            transition(':enter', [style({ opacity: 0, transform: 'scale(0.95)' }), animate('100ms ease-out', style({ opacity: 1, transform: 'scale(1)' }))]),
            transition(':leave', [animate('75ms', style({ opacity: 0, transform: 'scale(0.95)' }))]),
        ]),
    ],
})
export class CustomersComponent {
    @ViewChild('addContactModal') addContactModal!: ModalComponent;
    userData: User | null = null;
    businessId : string | null = null;

    displayType = 'list';
    params: FormGroup;
    filterdContactsList: any = [];
    searchUser = '';
    contactList: any  = [];

    constructor(public route: ActivatedRoute,public formBuilder: FormBuilder,private userService: UserService,private customerService: CustomerService,) {
        this.params = this.formBuilder.group({
            id: [0],
            name: ['', Validators.required],
            email: ['', Validators.compose([Validators.required, Validators.email])],
            role: ['', Validators.required],
            phone: ['', Validators.required],
            location: [''],
        });
    }
    
    initForm() {
        this.params = this.formBuilder.group({
            id: [0],
            name: ['', Validators.required],
            email: ['', Validators.compose([Validators.required, Validators.email])],
            role: ['', Validators.required],
            phone: ['', Validators.required],
            location: [''],
        });
    }

    ngOnInit() {
        this.userService.getUserData().subscribe(userData => {
            this.userData = userData;
          });
        this.getCustomers();
        this.searchContacts();

    }

    async getCustomers() {
        try {

            this.businessId = this.route.snapshot.paramMap.get('id');

            const customers: Customer[] = await lastValueFrom(this.customerService.getAllCustomers(this.businessId? this.businessId : ''));//this.userData.uuid
            this.filterdContactsList = [...customers];

            console.log(customers);

        } catch (error) {
            console.error('Error fetching customers', error);
        }
    }

    searchContacts() {
        this.filterdContactsList = this.contactList.filter((d:any) => d.name.toLowerCase().includes(this.searchUser.toLowerCase()));
    }

    editUser(user: any = null) {
        this.addContactModal.open();
        if (user) {
            this.params.setValue({
                id: user.id,
                name: user.name,
                email: user.email,
                role: user.role,
                phone: user.phone,
                location: user.location,
            });
        }
    }

    saveUser() {
        if (this.params.controls['name'].errors) {
            this.showMessage('Name is required.', 'error');
            return;
        }
        if (this.params.controls['email'].errors) {
            this.showMessage('Email is required.', 'error');
            return;
        }
        if (this.params.controls['phone'].errors) {
            this.showMessage('Phone is required.', 'error');
            return;
        }
        if (this.params.controls['role'].errors) {
            this.showMessage('Occupation is required.', 'error');
            return;
        }

        if (this.params.value.id) {
            //update user
            let user: any = this.contactList.find((d:any) => d.id === this.params.value.id);
            user.name = this.params.value.name;
            user.email = this.params.value.email;
            user.role = this.params.value.role;
            user.phone = this.params.value.phone;
            user.location = this.params.value.location;
        } else {
            //add user
            let maxUserId = this.contactList.length
                ? this.contactList.reduce((max:any, character:any) => (character.id > max ? character.id : max), this.contactList[0].id)
                : 0;

            let user = {
                id: maxUserId + 1,
                path: 'profile-35.png',
                name: this.params.value.name,
                email: this.params.value.email,
                role: this.params.value.role,
                phone: this.params.value.phone,
                location: this.params.value.location,
                posts: 20,
                followers: '5K',
                following: 500,
            };
            this.contactList.splice(0, 0, user);
            this.searchContacts();
        }

        this.showMessage('User has been saved successfully.');
        this.addContactModal.close();
    }

    deleteUser(user: any = null) {
        this.contactList = this.contactList.filter((d:any) => d.id != user.id);
        this.searchContacts();
        this.showMessage('User has been deleted successfully.');
    }

    showMessage(msg = '', type = 'success') {
        const toast: any = Swal.mixin({
            toast: true,
            position: 'top',
            showConfirmButton: false,
            timer: 3000,
            customClass: { container: 'toast' },
        });
        toast.fire({
            icon: type,
            title: msg,
            padding: '10px 20px',
        });
    }
}