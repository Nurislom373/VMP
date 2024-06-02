import {Injectable} from "@angular/core";
import {SidebarItem} from "./model/sidebar.item";

@Injectable({
  providedIn: 'root'
})
export class SidebarService {

  currentOpenPath: string = "";
  items: SidebarItem[] = [
    {
      name: 'Products',
      hasChildItems: true,
      childItems: [
        {
          name: 'Category',
          hasChildItems: false,
          path: '/category',
          isOpen: false,
        },
        {
          name: 'Tag',
          hasChildItems: false,
          path: '/tag',
          isOpen: false,
        },
        {
          name: 'Product',
          hasChildItems: false,
          path: '/product',
          isOpen: false,
        },
      ]
    },
  ];

  /**
   *
   */
  public getSidebarItems(): SidebarItem[] {
    console.log('get items ' + this.items.length);
    return this.items;
  }

  /**
   *
   * @param path
   */
  public changeSidebarOpenPath(path: string) {
    this.changeOpenPathValue(this.currentOpenPath, false);
    this.changeOpenPathValue(path, true);
    this.currentOpenPath = path;
  }

  /**
   *
   * @param path
   * @param isOpen
   */
  changeOpenPathValue(path: string, isOpen: boolean) {
    this.items.forEach(item => {
      if (item.hasChildItems) {
        item.childItems?.forEach(childItem => {
          this.changeStatusSidebarItem(childItem, path, isOpen);
        })
        return;
      }
      this.changeStatusSidebarItem(item, path, isOpen);
    })
  }

  /**
   *
   * @param item
   * @param path
   * @param isOpen
   */
  changeStatusSidebarItem(item: SidebarItem, path: string, isOpen: boolean) {
    if (item.path === path) {
      item.isOpen = isOpen;
    }
  }
}
