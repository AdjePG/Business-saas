import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
	selector: 'app-invitation',
	templateUrl: './invitation.component.html',
})
export class InvitationComponent {
	invitationId: string;

  	constructor(
		private router : Router,
        private route: ActivatedRoute
	) {
        this.invitationId = this.route.snapshot.paramMap.get('id') ?? "";
		localStorage.setItem("invitation-token", this.invitationId)
		router.navigate(["/"])
	}
}
