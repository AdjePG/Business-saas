import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnBusinessComponent } from './own-business.component';

describe('OwnBusinessComponent', () => {
  let component: OwnBusinessComponent;
  let fixture: ComponentFixture<OwnBusinessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OwnBusinessComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OwnBusinessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
