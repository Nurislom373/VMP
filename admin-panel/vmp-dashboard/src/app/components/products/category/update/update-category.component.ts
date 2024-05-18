import {Component} from '@angular/core';
import {CategoriesService} from "../../../../services/categories.service";
import {CommonModule} from "@angular/common";
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContainer,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatButton, MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'app-update-category',
  standalone: true,
  imports: [CommonModule, MatDialogActions, MatButton, MatDialogClose, MatDialogContent, MatButtonModule, MatDialogContainer, MatDialogTitle],
  templateUrl: './update-category.component.html',
  styleUrl: './update-category.component.css'
})
export class UpdateCategoryComponent {

  constructor(
    private categoriesService: CategoriesService,
    private dialogRef: MatDialogRef<UpdateCategoryComponent>
  ) {
  }

  getCategoryStatuses() {
    return this.categoriesService.getCategories()
  }
}
