import { Component, OnInit } from '@angular/core';
import { BusinessService } from 'src/app/service/business/business.service';
import { UserService } from 'src/app/service/user/user.service';
import { User } from 'src/app/models/user';
import { lastValueFrom } from 'rxjs';
import { BusinessElementType } from 'src/app/shared/types';
import { Business } from 'src/app/models/business';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
    ownBusinesses: Business[] = [];
    sharedBusinesses: Business[] = [];
    userData: User | null = null;

	faqsList = [
		{
			question: "¿Cómo creo mi propio negocio?",
			answer: `
				Para crear un negocio en la plataforma de FactuLink, necesitarás primero ir a la página de 'Mis negocios'.
				Una vez ahí, deberás hacer click en el botón de 'Añadir negocio' para que aparezca un formulario, el cual,
				se tendrá que rellenar los datos que se soliciten.
			`
		},
		{
			question: "¿Cómo invito a un usuario a mi negocio?",
			answer: `
				Para enviar una solicitud de invitación a una persona, deberás pulsar el botón de compartir de un negocio
				tuyo. Una vez se pulse el botón, se abrirá un formulario donde tendrás que poner una dirección de correo
				electrónico del destinatario. 
			`
		},
		{
			question: "¿Cómo formo parte de un negocio compartido?",
			answer: `
				Para formar parte de un negocio de otra persona, tendrás que recibir una solicitud de invitación que lo
				enviará el propietario. Si recibes una solicitud de invitación en tu correo electrónico y quieres entrar
				al negocio, tendrás que aceptar la invitación iniciando sesión con tu cuenta deseada.
			`
		}
	]

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

            this.ownBusinesses = response.result;

			response = await lastValueFrom(this.businessService.getBusinesses(this.userData.uuid, true))

			if (response.status !== 200) {
				console.error(response.message);
				return;
			}

            this.sharedBusinesses = response.result;
        } catch (error) {
            console.error('Error fetching businesses', error);
        }
    }

	handleBusinessAdded(business: Business) {
		this.ownBusinesses.push(business);
	}

	handleBusinessUpdated(business : Business) {
		let index;

		index = this.ownBusinesses.findIndex(ownBusiness => ownBusiness.uuid === business.uuid);

		if (index !== -1) {
			this.ownBusinesses[index] = business
			return
		}

		index = this.sharedBusinesses.findIndex(sharedBusiness => sharedBusiness.uuid === business.uuid);

		if (index !== -1) {
			this.sharedBusinesses[index] = business
			return
		}
	}
	
	handleBusinessDeleted(business : { uuid: string, isOwnBusiness: boolean }) {
		if (business.isOwnBusiness) {
			this.ownBusinesses = this.ownBusinesses.filter(ownBusiness => ownBusiness.uuid !== business.uuid);
		} else {
			this.sharedBusinesses = this.sharedBusinesses.filter(sharedBusiness => sharedBusiness.uuid !== business.uuid);
		}
	}
}
