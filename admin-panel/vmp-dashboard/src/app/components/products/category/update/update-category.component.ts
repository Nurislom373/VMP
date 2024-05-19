import {Component, Inject, ViewChild} from '@angular/core';
import {CommonModule} from "@angular/common";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContainer,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatButton, MatButtonModule} from "@angular/material/button";
import {CategoryForm} from "../../../../models/form/category.form";
import {FormsModule, NgForm, ReactiveFormsModule} from "@angular/forms";
import {CategoryService} from "../service/category.service";
import {CategoryBadge} from "../../../../models/category";
import {CATEGORY_KEY} from "../../../../core/global.constants";
import {StateActionRegistry} from "../../../../services/state.action.registry";

@Component({
  selector: 'update-category',
  standalone: true,
  imports: [CommonModule, MatDialogActions, MatButton, MatDialogClose, MatDialogContent, MatButtonModule, MatDialogContainer, MatDialogTitle, ReactiveFormsModule, FormsModule],
  templateUrl: './update-category.component.html',
  styleUrl: './update-category.component.css'
})
export class UpdateCategoryComponent {

  updateCategoryForm: CategoryForm = new CategoryForm();

  @ViewChild("categoryForm")
  CategoryForm!: NgForm;

  constructor(
    private stateActionRegistry: StateActionRegistry,
    private categoryService: CategoryService,
    private dialogRef: MatDialogRef<UpdateCategoryComponent>,
    @Inject(MAT_DIALOG_DATA) public category: CategoryBadge
  ) {
    this.updateCategoryForm.id = category.id;
    this.updateCategoryForm.name = category.name;
    this.updateCategoryForm.status = category.status?.valueOf()
  }

  getCategoryStatuses() {
    return this.categoryService.getCategoryStatuses()
  }

  updateCategory() {
    this.categoryService.update(this.updateCategoryForm, this.updateCategoryForm.id!)
      .subscribe(response => {
        if (response.ok) {
          this.loadCategoriesAction();
        }
      })
  }

  private loadCategoriesAction() {
    this.stateActionRegistry.executeAction(CATEGORY_KEY);
  }
}
