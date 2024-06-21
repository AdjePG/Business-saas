import { Component } from '@angular/core';
import { slideDownUp } from './shared/animations';

@Component({
    moduleId: module.id,
    templateUrl: './index.html',
    animations: [slideDownUp]
})
export class IndexComponent {
    constructor() { }
    activeTab: any = 'general';
    active1: any = 1;
    active2: any = 1;
    modal = false;
    items = [
        {
            src: '/assets/images/knowledge/image-5.jpg',
            title: 'Excessive sugar is harmful',
        },
        {
            src: '/assets/images/knowledge/image-5.jpg',
            title: 'Excessive sugar is harmful',
        },
        {
            src: '/assets/images/knowledge/image-5.jpg',
            title: 'Excessive sugar is harmful',
        },
        {
            src: '/assets/images/knowledge/image-5.jpg',
            title: 'Excessive sugar is harmful',
        }
    ];
}
