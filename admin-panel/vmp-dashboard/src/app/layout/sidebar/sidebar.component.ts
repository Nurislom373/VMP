import { Component } from '@angular/core';
import {MatDrawer} from "@angular/material/sidenav";
import {
  NzContentComponent,
  NzFooterComponent,
  NzHeaderComponent,
  NzLayoutComponent,
  NzSiderComponent
} from "ng-zorro-antd/layout";
import {NzIconDirective} from "ng-zorro-antd/icon";
import {NzBreadCrumbComponent, NzBreadCrumbItemComponent} from "ng-zorro-antd/breadcrumb";
import {NzMenuDirective, NzMenuItemComponent, NzSubMenuComponent} from "ng-zorro-antd/menu";
import { NzIconModule } from 'ng-zorro-antd/icon';
import {NzAvatarComponent} from "ng-zorro-antd/avatar";
import {SidebarService} from "./sidebar.service";
import {NgForOf, NgIf} from "@angular/common";
import {SidebarItem} from "./model/sidebar.item";

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    MatDrawer,
    NzLayoutComponent,
    NzSiderComponent,
    NzHeaderComponent,
    NzIconDirective,
    NzContentComponent,
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    NzFooterComponent,
    NzSubMenuComponent,
    NzMenuItemComponent,
    NzMenuDirective,
    NzIconModule,
    NzAvatarComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {

  constructor(private sidebarService: SidebarService) {
  }

  getSidebarItems(): SidebarItem[] {
    return this.sidebarService.getSidebarItems();
  }
}
