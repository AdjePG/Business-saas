import { Component, Input } from '@angular/core';
import { slideDownUp } from 'src/app/shared/animations';

@Component({
	selector: 'faqs',
	templateUrl: './faqs.component.html',
    animations: [slideDownUp],
})
export class FaqsComponent {
    @Input() faqs: any;
    
    activeFaq: any = 0;
}
