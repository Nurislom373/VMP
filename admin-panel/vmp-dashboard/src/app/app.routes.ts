import {CanActivateFn, Routes} from '@angular/router';
import {CategoryComponent} from "./components/products/category/category.component";
import {HomeComponent} from "./home/home.component";
import {inject} from "@angular/core";
import {AuthGuard} from "./auth/auth.guard";

const isAuthenticated: CanActivateFn = (route, state) => {
  return inject(AuthGuard).isAccessAllowed(route, state);
}

export const routes: Routes = [
  {
    path: '',
    canActivate: [isAuthenticated],
    component: HomeComponent
  },
  {
    path: 'category',
    component: CategoryComponent
  }
];
