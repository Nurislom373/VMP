import {Component, ViewChild} from "@angular/core";
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
import {CategoryForm} from "../../../../models/form/category.form";
import {FormsModule, NgForm} from "@angular/forms";
import {CategoryService} from "../service/category.service";
import {Category, CategoryStatus} from "../../../../models/category";
import {StateActionRegistry} from "../../../../services/state.action.registry";
import {CATEGORY_KEY} from "../../../../core/global.constants";

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
    private stateActionRegistry: StateActionRegistry,
    private categoryService: CategoryService,
    private dialogRef: MatDialogRef<CreateCategoryComponent>
  ) {
  }

  getCategoryStatuses() {
    return this.categoryService.getCategoryStatuses()
  }

  createCategory() {
    this.categoryService.create(this.toEntity(this.addCategoryForm))
      .subscribe(response => {
        if (response.status == 201) {
          this.loadCategoriesAction();
        }
      })
  }

  toEntity(categoryForm: CategoryForm): Category {
    return {
      name: categoryForm.name,
      status: categoryForm.status as CategoryStatus
    };
  }

  private loadCategoriesAction() {
    this.stateActionRegistry.executeAction(CATEGORY_KEY);
  }
}
