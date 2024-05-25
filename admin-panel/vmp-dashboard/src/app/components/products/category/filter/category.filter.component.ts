import {Component} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {CategoryService} from "../service/category.service";

@Component({
  selector: 'category-filter',
  standalone: true,
  templateUrl: './category.filter.component.html',
  imports: [
    FormsModule,
    NgForOf
  ],
  styleUrl: './category.filter.component.css'
})
export class CategoryFilterComponent {

  constructor(private categoryService: CategoryService) {
  }

  getCategoryStatuses() {
    return this.categoryService.getCategoryStatuses()
  }

}
