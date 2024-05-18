import {Component, OnInit, ViewChild} from '@angular/core';
import {CategoriesService} from "../../../services/categories.service";
import {Category, CategoryBadge, CategoryStatus} from "../../../models/category";
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

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [CommonModule, NavbarComponent, SidebarComponent, FormsModule, UpdateCategoryComponent, MatButton],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css'
})
export class CategoryComponent implements OnInit {

  private readonly _url = 'http://localhost:8083/api/categories';

  @ViewChild("CategoryForm")
  CategoryForm!: NgForm;

  categories: CategoryBadge[] = [];
  categoryForm: CategoryForm = new CategoryForm();
  perPageElementSize: number = 10
  categoriesCount: number = 0
  currentPage: number = 0
  pagesRange: Array<Pair> = []

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
    let pages = Math.ceil(this.categoriesCount / this.perPageElementSize);
    console.log(`pages count ${pages}`)
    if ((pages - 1) > this.currentPage) {
      this.currentPage++;
      this.loadCategories()
    }
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

  calculateElementsPerPage(totalElements: number, totalPages: number): Array<Pair> {
    const elementsPerPage = Math.ceil(totalElements / totalPages);
    const result: Array<Pair> = [];

    let startIndex = 0;
    let endIndex = 0;

    for (let i = 1; i <= totalPages; i++) {
      startIndex = endIndex + 1;
      endIndex = Math.min(totalElements, i * elementsPerPage);
      const pair = new Pair(startIndex, endIndex);
      result.push(pair);
    }

    return result;
  }
}
