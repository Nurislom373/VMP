import {Component, OnInit, ViewChild} from '@angular/core';
import {CategoriesService} from "../../../services/categories.service";
import {CategoryBadge} from "../../../models/category";
import {CommonModule} from "@angular/common";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../../auth/auth.service";
import {initFlowbite} from "flowbite";
import {NavbarComponent} from "../../../layout/navbar/navbar.component";
import {SidebarComponent} from "../../../layout/sidebar/sidebar.component";
import {CategoryForm} from "../../../models/form/category.form";
import {FormsModule, NgForm} from "@angular/forms";
import {Pair} from "../../../models/pair";
import {UpdateCategoryComponent} from "./update/update-category.component";
import {MatDialog} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";
import {CreateCategoryComponent} from "./create/create-category.component";
import {CategoryService} from "./service/category.service";
import {DeleteCategoryComponent} from "./delete/delete-category.component";

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [CommonModule, NavbarComponent, SidebarComponent, FormsModule, UpdateCategoryComponent, MatButton],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css'
})
export class CategoryComponent implements OnInit {

  @ViewChild("CategoryForm")
  CategoryForm!: NgForm;

  categories: CategoryBadge[] = [];
  categoryForm: CategoryForm = new CategoryForm();
  perPageElementSize: number = 10
  categoriesCount: number = 0
  currentPage: number = 0
  pagesRange: Array<Pair> = [];
  pagesNumber: Array<number> = [];

  constructor(
    private categoriesService: CategoriesService,
    private categoryService: CategoryService,
    private httpClient: HttpClient,
    private authService: AuthService,
    private dialog: MatDialog
  ) {
  }

  getCategoryStatuses() {
    return this.categoriesService.getCategoryStatuses()
  }

  nextPage() {
    let pages = this.getPageCount();
    console.log(`pages count ${pages}`)
    if ((pages - 1) > this.currentPage) {
      this.currentPage++;
      this.loadCategories()
    }
  }

  private getPageCount() {
    return Math.ceil(this.categoriesCount / this.perPageElementSize);
  }

  previousPage() {
    if (0 < this.currentPage) {
      this.currentPage--;
      this.loadCategories()
    }
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
    initFlowbite();

    let headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Access-Control-Allow-Headers': 'Content-Type',
      'Authorization': ''
    }

    this.authService.keycloak.getToken()
      .then(token => {
        console.log('Token : ' + token)
        headers['Authorization'] = 'Bearer ' + token
      })

    this.loadCategories();
  }

  private loadCategories() {
    this.categoryService.getAll(`size=${this.perPageElementSize}&page=${this.currentPage}&sort=id,desc`)
      .subscribe(response => {
        if (response.ok) {
          this.categories = this.categoriesService.mapCategoriesToCategoriesBadge(response.body!);
        }
      })

    this.categoryService.count()
      .subscribe(response => {
        if (response.ok) {
          this.categoriesCount = response.body!;
        }
      })
  }

  calculateElementsPerPage(totalElements: number, totalPages: number) {

    let pageCount = this.getPageCount();
    let currentPage = this.currentPage;

    const result: Array<number> = [];

    for (let i = 1; i <= 5; i++) {
      let prePageNum = currentPage++;

      if (currentPage == 0) {
        result.push(prePageNum);
      }
    }


  }
}
