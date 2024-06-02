export interface SidebarItem {
  name: string;
  hasChildItems: boolean;
  childItems?: SidebarItem[];
  path?: string;
  isOpen?: boolean;
}
