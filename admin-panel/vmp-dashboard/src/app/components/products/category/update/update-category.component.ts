import {Component, OnInit} from '@angular/core';
import {CategoriesService} from "../../../../services/categories.service";
import {CommonModule} from "@angular/common";
import {initFlowbite} from "flowbite";
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef} from "@angular/material/dialog";
import {MatButton, MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'app-update-category',
  standalone: true,
  imports: [CommonModule, MatDialogActions, MatButton, MatDialogClose, MatDialogContent, MatButtonModule],
  templateUrl: './update-category.component.html',
  styleUrl: './update-category.component.css'
})
export class UpdateCategoryComponent implements OnInit {

  constructor(
    private categoriesService: CategoriesService,
    private dialogRef: MatDialogRef<UpdateCategoryComponent>
  ) {
  }

  ngOnInit(): void {
    initFlowbite();
  }

  getCategoryStatuses() {
    return this.categoriesService.getCategories()
  }
}
