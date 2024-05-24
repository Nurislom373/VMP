import {Component, OnInit, ViewChild} from '@angular/core';
import {CategoryBadge} from "../../../models/category";
import {CommonModule} from "@angular/common";
import {NavbarComponent} from "../../../layout/navbar/navbar.component";
import {SidebarComponent} from "../../../layout/sidebar/sidebar.component";
import {FormsModule, NgForm} from "@angular/forms";
import {UpdateCategoryComponent} from "./update/update-category.component";
import {MatDialog} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";
import {CreateCategoryComponent} from "./create/create-category.component";
import {CategoryService} from "./service/category.service";
import {DeleteCategoryComponent} from "./delete/delete-category.component";
import {StateAction} from "../../../services/state.action";
import {StateActionRegistry} from "../../../services/state.action.registry";
import {StateActionNode} from "../../../models/state.action.node";
import {CATEGORY_KEY} from "../../../core/global.constants";
import {CategoryStatusComponent} from "./status/category.status.component";

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [CommonModule, NavbarComponent, SidebarComponent, FormsModule, UpdateCategoryComponent, MatButton, CategoryStatusComponent],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css'
})
export class CategoryComponent implements OnInit, StateAction {

  @ViewChild("CategoryForm")
  CategoryForm!: NgForm;

  categories: CategoryBadge[] = [];
  pageSize: number = 10
  collectionSize: number = 0
  currentPage: number = 1
  totalPages: any[] = [];

  constructor(
    private stateActionRegistry: StateActionRegistry,
    private categoryService: CategoryService,
    private dialog: MatDialog
  ) {
  }

  action(): void {
    this.loadCategories()
  }

  /** Set next page number */
  next() {
    const nextPage = this.currentPage + 1;
    nextPage <= this.totalPages.length && this.setPageNumber(nextPage);
    this.loadCategories();
  }

  /** Set previous page number */
  previous() {
    const previousPage = this.currentPage - 1;
    previousPage >= 1 && this.setPageNumber(previousPage);
    this.loadCategories();
  }

  selectPageNumber(pageNumber: number) {
    this.setPageNumber(pageNumber);
    this.loadCategories();
  }

  /** Set page number */
  setPageNumber(pageNumber: number) {
    this.currentPage = pageNumber;
  }

  private getPageCount() {
    return Math.ceil(this.collectionSize / this.pageSize);
  }

  addCategory() {
    this.dialog.open(CreateCategoryComponent, {
      height: '450px',
      width: '1000px',
      position: {right: '20%', left: '22%', top: '5%'},
      panelClass: 'rounded-lg'
    })
  }

  updateCategory(category: CategoryBadge) {
    this.dialog.open(UpdateCategoryComponent, {
      data: {
        id: category.id,
        name: category.name,
        status: category.status
      },
      height: '450px',
      width: '1000px',
      position: {right: '20%', left: '22%', top: '5%'},
      panelClass: 'rounded-lg'
    })
  }

  deleteCategory(category: CategoryBadge) {
    this.dialog.open(DeleteCategoryComponent, {
      data: {
        id: category.id,
        name: category.name,
        status: category.status
      },
      height: '230px',
      width: '1000px',
      position: {right: '20%', left: '22%', top: '2%'},
      panelClass: 'rounded-lg'
    })
  }

  ngOnInit(): void {
    this.loadCategories();
    this.stateActionRegistry.add(new StateActionNode(CATEGORY_KEY, this));
  }

  private loadCategories() {
    this.categoryService.getAll(`size=${this.pageSize}&page=${this.currentPage - 1}&sort=id,desc`)
      .subscribe(response => {
        if (response.ok) {
          this.categories = this.categoryService.mapCategoriesToCategoriesBadge(response.body!);
        }
      })

    this.categoryService.count()
      .subscribe(response => {
        if (response.ok) {
          this.collectionSize = response.body!;
          this.totalPages = new Array(this.getPageCount());
        }
      })
  }
}
