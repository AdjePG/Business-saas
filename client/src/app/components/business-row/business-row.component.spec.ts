import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BusinessRowComponent } from './business-row.component';

describe('BusinessTableComponent', () => {
  let component: BusinessRowComponent;
  let fixture: ComponentFixture<BusinessRowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BusinessRowComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BusinessRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
