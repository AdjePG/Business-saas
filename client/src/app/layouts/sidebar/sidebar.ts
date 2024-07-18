import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateService } from '@ngx-translate/core';
import { slideDownUp } from '../../shared/animations';
import { IconUserComponent } from 'src/app/shared/icon/icon-user';
import { IconMenuComponentsComponent } from 'src/app/shared/icon/menu/icon-menu-components';
import { IconMenuNotesComponent } from 'src/app/shared/icon/menu/icon-menu-notes';
import { IconMenuDashboardComponent } from 'src/app/shared/icon/menu/icon-menu-dashboard';
import { IconMenuUsersComponent } from 'src/app/shared/icon/menu/icon-menu-users';
import { IconMenuWidgetsComponent } from 'src/app/shared/icon/menu/icon-menu-widgets';

@Component({
    selector: 'sidebar',
    templateUrl: './sidebar.html',
    animations: [slideDownUp],
})
export class SidebarComponent {
    items : any[] = []
    businessId : string | null = null;

    active = false;
    store: any;
    activeDropdown: string[] = [];
    parentDropdown: string = '';
    
    constructor(
        public translate: TranslateService, 
        public storeData: Store<any>, 
        public router: Router,
        private route: ActivatedRoute
    ) {
        this.initStore();
    }
    async initStore() {
        this.storeData
            .select((d) => d.index)
            .subscribe((d) => {
                this.store = d;
            });
    }

    ngOnInit() {
        this.businessId = this.route.snapshot.paramMap.get('id');

        if (this.businessId) {
            this.setDashboardNavLinks();
        } else {
            this.setDefaultNavLinks();
        }
    }

    toggleMobileMenu() {
        if (window.innerWidth < 1024) {
            this.storeData.dispatch({ type: 'toggleSidebar' });
        }
    }

    toggleAccordion(name: string, parent?: string) {
        if (this.activeDropdown.includes(name)) {
            this.activeDropdown = this.activeDropdown.filter((d) => d !== name);
        } else {
            this.activeDropdown.push(name);
        }
    }

    setDashboardNavLinks() {
        this.items = [
            {
                title: undefined,
                navLinks: [
                    {
                        icon: {
                            component: IconMenuWidgetsComponent
                        },
                        name: "Panel principal",
                        route: `/${this.businessId}`
                    }
                ]
            },
            {
                title: "Datos Maestros",
                navLinks: [
                    {
                        icon: {
                            component: IconMenuComponentsComponent,
                        },
                        name: "Artículos y servicios",
                        route: `/${this.businessId}/items`
                    },
                    {
                        icon: {
                            component: IconUserComponent,
                            fill: true
                        },
                        name: "Clientes",
                        route: `/${this.businessId}/customers`
                    },
                    
                ]
            },
            {
                title: "Documentos",
                navLinks: [
                    {
                        icon: {
                            component: IconMenuNotesComponent
                        },
                        name: "Facturas",
                        route: `/${this.businessId}/invoices`
                    }
                ]
            }
        ]
    }
  
    setDefaultNavLinks() {
        this.items = [
            {
                title: undefined,
                navLinks: [
                    {
                        icon: {
                            component: IconMenuDashboardComponent
                        },
                        name: "Inicio",
                        route: `/`
                    },
                    {
                        icon: {
                            component: IconMenuComponentsComponent
                        },
                        name: "Mis negocios",
                        route: `/own-business`
                    },
                    {
                        icon: {
                            component: IconMenuUsersComponent
                        },
                        name: "Negocios compartidos",
                        route: `/shared-business`
                    }
                ]
            }
        ]
    }
}
