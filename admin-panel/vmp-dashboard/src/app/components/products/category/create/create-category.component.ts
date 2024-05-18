import {Component, ViewChild} from "@angular/core";
import {CommonModule} from "@angular/common";
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContainer,
  MatDialogContent, MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatButton, MatButtonModule} from "@angular/material/button";
import {CategoriesService} from "../../../../services/categories.service";
import {CategoryForm} from "../../../../models/form/category.form";
import {FormsModule, NgForm} from "@angular/forms";

@Component({
  selector: 'create-category',
  standalone: true,
  imports: [CommonModule, MatDialogActions, MatButton, MatDialogClose, MatDialogContent, MatButtonModule, MatDialogContainer, MatDialogTitle, FormsModule],
  templateUrl: './create-category.component.html',
  styleUrl: './create-category.component.css'
})
export class CreateCategoryComponent {

  addCategoryForm: CategoryForm = new CategoryForm();

  @ViewChild("categoryForm")
  CategoryForm!: NgForm;

  constructor(
    private categoriesService: CategoriesService,
    private dialogRef: MatDialogRef<CreateCategoryComponent>
  ) {
  }

  getCategoryStatuses() {
    return this.categoriesService.getCategories()
  }

  createCategory() {
    console.log(this.addCategoryForm);
  }
}
