import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CardType } from 'src/app/shared/types';

@Component({
  selector: 'card',
  templateUrl: './card.component.html'
})
export class CardComponent {
  @Input() cardType: CardType = CardType.INFO
  @Input() title: string = ""
  @Input() image: string = ""

  @Output() clickEvent: EventEmitter<void> = new EventEmitter<void>();

  onClick() {
    this.clickEvent.emit();
  }

  getType() {
    return this.cardType === CardType.INFO
  }
}
