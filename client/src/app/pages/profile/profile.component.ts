import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalComponent } from 'angular-custom-modal';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/service/user/user.service';
import { showAlert } from 'src/app/shared/alerts';
import { compareWithWord } from 'src/app/shared/custom-validators';
import { ToastType } from 'src/app/shared/types';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {
  @ViewChild('profilePhotoModal') profilePhotoModal!: ModalComponent;

  profileDetailsForm: FormGroup;
  profilePhotoForm: FormGroup;
  profileNewEmailForm: FormGroup;
  profileNewPassForm: FormGroup;
  profileDeleteForm: FormGroup;

  activeTab : string = "general"
  uuid : string = ""
  email : string = ""
  submitted: boolean = false

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService
  ) {
    this.userService.getUserData().subscribe(userData => {
      this.uuid = userData?.uuid || '';
      this.email = userData?.email || '';
    });

    this.profileDetailsForm = this.formBuilder.group({
      name: ['', Validators.required],
      firstSurname: [''],
      lastSurname: [''],
      country: [''],
      location: [''],
      address: [''],
      phone: [''],
      contactEmail: ['']
    });

    this.profilePhotoForm = this.formBuilder.group({
      photo: ['', Validators.required]
    });

    this.profileNewEmailForm = this.formBuilder.group({
      email: ['', Validators.required]
    });

    this.profileNewPassForm = this.formBuilder.group({
      currentPass: ['', Validators.required],
      newPass: ['', Validators.required],
      validateNewPass: ['', Validators.required]
    });

    this.profileDeleteForm = this.formBuilder.group({
      word: ['', [Validators.required, compareWithWord(this.email)]]
    }, {updateOn: 'submit'});
  }

  ngOnInit(): void {
    this.userService.getUser(this.uuid).subscribe({
      next: (response) => {
        this.profileDetailsForm.patchValue(response.result);
      }
    });
  }

  updateUser() {
    if (this.profileDetailsForm.invalid) {
      return;
    }

    const user: User = {
      ...this.profileDetailsForm.value,
      ...this.profilePhotoForm.value
    };

    this.userService.updateUser(user).subscribe({
      next: (response) => {
        showAlert({
          toastType: ToastType.SUCCESS,
          message: 'Los datos del usuario se han actualizado correctamente.'
        });
      },
      error: (error) => {
        showAlert({
          toastType: ToastType.ERROR,
          message: error.error.message
        });
      }
    })
  }

  deleteUser() {
    this.submitted = true

    if (this.profileDeleteForm.invalid) {
      return;
    }

    const user: User = {
      uuid: this.uuid
    };
    

    this.userService.deleteUser().subscribe({
      next: (response) => {
        localStorage.removeItem("user-auth")
        window.location.reload()
      },
      error: (error) => {
        showAlert({
          toastType: ToastType.ERROR,
          message: error.error.message
        });
      }
    })
  }

  openModal(modal: ModalComponent) {
    this.submitted = false
    modal.open()
  }

  closeModal(modal: ModalComponent, form: FormGroup) {
    modal.close();
    form.reset();
  }
}
