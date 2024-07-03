import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarNavLinkComponent } from './sidebar-nav-link.component';

describe('SidebarNavLinkComponent', () => {
  let component: SidebarNavLinkComponent;
  let fixture: ComponentFixture<SidebarNavLinkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SidebarNavLinkComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SidebarNavLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
