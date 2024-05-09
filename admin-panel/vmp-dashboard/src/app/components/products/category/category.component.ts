import {Component, OnInit} from '@angular/core';
import {CategoriesService} from "../../../services/categories.service";
import {Category, CategoryStatus} from "../../../models/category";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css'
})
export class CategoryComponent implements OnInit {

  categories: Category[] = [];

  constructor(private categoriesService: CategoriesService) {
  }

  ngOnInit(): void {
    // this.categoriesService.getCategories('http://localhost:8082/api/categories')
    //   .subscribe((category) => {
    //     console.log(category)
    //   })
    this.categories = [
      {
        id: 1,
        name: 'IT',
        status: CategoryStatus.ACTIVE
      },
      {
        id: 2,
        name: 'Dinay',
        status: CategoryStatus.DELETED
      },
      {
        id: 2,
        name: 'Eren',
        status: CategoryStatus.BLOCKED
      }
    ]
  }
}
