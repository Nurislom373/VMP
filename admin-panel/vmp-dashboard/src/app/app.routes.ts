import {Routes} from '@angular/router';
import {CategoryComponent} from "./components/products/category/category.component";
import {HomeComponent} from "./home/home.component";

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'category',
    component: CategoryComponent
  }
];
