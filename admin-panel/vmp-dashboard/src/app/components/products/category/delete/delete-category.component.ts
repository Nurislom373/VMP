import {Component, Inject} from "@angular/core";
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
import {FormsModule} from "@angular/forms";
import {CategoryService} from "../service/category.service";
import {CategoryBadge} from "../../../../models/category";
import {StateActionRegistry} from "../../../../services/state.action.registry";
import {CATEGORY_KEY} from "../../../../core/global.constants";
import {
  CustomNoneOutlineButtonComponent
} from "../../../../core/buttons/custom-none-outline-button/custom-none-outline-button.component";
import {
  CustomPrimaryButtonComponent
} from "../../../../core/buttons/custom-primary-button/custom-primary-button.component";

@Component({
  selector: 'delete-category',
  standalone: true,
  imports: [CommonModule, MatDialogActions, MatButton, MatDialogClose, MatDialogContent, MatButtonModule, MatDialogContainer, MatDialogTitle, FormsModule, CustomNoneOutlineButtonComponent, CustomPrimaryButtonComponent],
  templateUrl: './delete-category.component.html',
  styleUrl: './delete-category.component.css'
})
export class DeleteCategoryComponent {

  constructor(
    private stateActionRegistry: StateActionRegistry,
    private categoryService: CategoryService,
    private dialogRef: MatDialogRef<DeleteCategoryComponent>,
    @Inject(MAT_DIALOG_DATA) public category: CategoryBadge
  ) {
  }

  deleteEvent() {
    this.categoryService.delete(this.category.id!)
      .subscribe(response => {
        if (response.status === 204) {
          this.loadCategoriesAction();
        }
      })
  }

  private loadCategoriesAction() {
    this.stateActionRegistry.executeAction(CATEGORY_KEY);
  }
}
