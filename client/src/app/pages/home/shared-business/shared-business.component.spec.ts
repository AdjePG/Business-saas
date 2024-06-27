import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedBusinessComponent } from './shared-business.component';

describe('SharedBusinessComponent', () => {
  let component: SharedBusinessComponent;
  let fixture: ComponentFixture<SharedBusinessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SharedBusinessComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SharedBusinessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
