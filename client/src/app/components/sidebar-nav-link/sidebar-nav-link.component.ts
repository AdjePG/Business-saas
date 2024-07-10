import { Component, ComponentRef, Input, OnInit, ViewChild, ViewContainerRef } from '@angular/core';

@Component({
  selector: 'app-sidebar-nav-link',
  templateUrl: './sidebar-nav-link.component.html'
})
export class SidebarNavLinkComponent implements OnInit {
  @ViewChild('icon', { read: ViewContainerRef, static: true }) container! : ViewContainerRef;
  @Input() navLink : any;

  constructor() {
  }

  ngOnInit(){
    this.container.clear();
    const componentRef: ComponentRef<any> = this.container.createComponent(this.navLink.icon.component);
    componentRef.instance.customClass = "shrink-0 group-hover:!text-primary";
    Object.keys(this.navLink.icon).forEach(input => {
      if (input !== "component") {
        componentRef.instance[input] = this.navLink.icon[input];
      }
    });
    
    this.setActiveDropdown()
  }

  setActiveDropdown() {
    const selector = document.querySelector('.sidebar ul a[routerLink="' + window.location.pathname + '"]');
    if (selector) {
      selector.classList.add('active');
      const ul: any = selector.closest('ul.sub-menu');
      if (ul) {
        let ele: any = ul.closest('li.menu').querySelectorAll('.nav-link') || [];
        if (ele.length) {
          ele = ele[0];
          setTimeout(() => {
              ele.click();
          });
        }
      }
    }
  }
}
