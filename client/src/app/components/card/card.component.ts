import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CardType } from 'src/app/shared/types';
import { getUuidUserFromToken } from 'src/app/shared/token-utils';
import { BusinessService } from 'src/app/service/business/business.service';
import { Business } from '../../models/business';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'card',
  templateUrl: './card.component.html'
})
export class CardComponent {
  @Input() cardType: CardType = CardType.INFO
  @Input() business!: Business;
  @Output() clickEvent: EventEmitter<void> = new EventEmitter<void>();
  @Output() deleteEvent: EventEmitter<number> = new EventEmitter<number>();

  constructor(private businessService: BusinessService) {
 
  }

  onClick() {
    this.clickEvent.emit();
  }

  getType() {
    return this.cardType === CardType.INFO
  }


  async deleteBusiness() {
    try {
      await firstValueFrom(this.businessService.deleteBusiness(this.business.uuid));
      this.deleteEvent.emit(this.business.uuid);
    } catch (error) {
      console.error('Error deleting business', error);
    }
  }
}
