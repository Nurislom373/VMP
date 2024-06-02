import {Component, OnInit, ViewChild} from '@angular/core';
import {CategoryBadge} from "../../../models/category";
import {CommonModule} from "@angular/common";
import {NavbarComponent} from "../../../layout/navbar/navbar.component";
import {SidebarComponent} from "../../../layout/sidebar/sidebar.component";
import {FormsModule, NgForm, ReactiveFormsModule} from "@angular/forms";
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
import {FilterModel} from "../../../models/filter/filter.model";
import {NzButtonComponent, NzButtonGroupComponent} from "ng-zorro-antd/button";
import {NzIconDirective} from "ng-zorro-antd/icon";
import {NzFlexDirective} from "ng-zorro-antd/flex";
import {NzInputDirective, NzInputGroupComponent} from "ng-zorro-antd/input";
import {
  NzCellBreakWordDirective,
  NzTableComponent,
  NzTableModule,
  NzTableQueryParams,
  NzThAddOnComponent
} from "ng-zorro-antd/table";
import {NzPaginationComponent} from "ng-zorro-antd/pagination";
import {NzCollapseComponent, NzCollapsePanelComponent} from "ng-zorro-antd/collapse";
import {NzFormControlComponent, NzFormDirective, NzFormItemComponent, NzFormLabelComponent} from "ng-zorro-antd/form";
import {NzColDirective, NzRowDirective} from "ng-zorro-antd/grid";
import {NzOptionComponent, NzSelectComponent} from "ng-zorro-antd/select";
import {NzSpaceComponent, NzSpaceItemDirective} from "ng-zorro-antd/space";
import {NzRadioComponent, NzRadioGroupComponent} from "ng-zorro-antd/radio";
import {
  CustomPrimaryButtonComponent
} from "../../../core/buttons/custom-primary-button/custom-primary-button.component";
import {CategoryForm} from '../../../models/form/category.form';
import {FieldType} from "../../../models/filter/field.type";

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [CommonModule, NzTableModule, NavbarComponent, SidebarComponent, FormsModule, UpdateCategoryComponent, MatButton, CategoryStatusComponent, NzButtonComponent, NzIconDirective, NzFlexDirective, NzInputGroupComponent, NzInputDirective, NzThAddOnComponent, NzCellBreakWordDirective, NzTableComponent, NzPaginationComponent, NzCollapseComponent, NzCollapsePanelComponent, ReactiveFormsModule, NzFormDirective, NzFormItemComponent, NzColDirective, NzRowDirective, NzFormLabelComponent, NzSelectComponent, NzOptionComponent, NzFormControlComponent, NzSpaceComponent, NzSpaceItemDirective, NzRadioGroupComponent, NzRadioComponent, NzButtonGroupComponent, CustomPrimaryButtonComponent],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css'
})
export class CategoryComponent implements OnInit, StateAction {

  filterForm: CategoryForm = new CategoryForm();

  @ViewChild("categoryForm")
  CategoryForm!: NgForm;

  isActiveFilterPanel = true;
  loading = false;

  categories: CategoryBadge[] = [];
  filterModels: FilterModel[] = [];

  sort: string = "id,desc";
  pageSize: number = 10
  totalPageSize: number = 0
  currentPage: number = 1

  constructor(
    private stateActionRegistry: StateActionRegistry,
    private categoryService: CategoryService,
    private dialog: MatDialog
  ) {
  }

  // Main Methods

  /**
   *
   */
  public getStatusArray() {
    return this.categoryService.getCategoryStatuses();
  }

  /**
   *
   * @param property
   */
  public changeProperties(property: any): void {
    this.filterModels = property;
    this.loadCategories();
  }

  /**
   *
   */
  public action(): void {
    this.loadCategories();
  }

  /**
   *
   * @param pageNumber
   */
  public changePageIndex(pageNumber: number) {
    this.setPageNumber(pageNumber);
    this.loadCategories();
  }

  /**
   *
   * @param params
   */
  public onQueryParamsChange(params: NzTableQueryParams) {
    console.log(params);
  }

  /** Set page number */
  public setPageNumber(pageNumber: number) {
    this.currentPage = pageNumber;
  }

  /**
   *
   */
  public getPageCount() {
    return Math.ceil(this.totalPageSize / this.pageSize);
  }

  // Events

  openCloseFilterModalEvent() {
    this.isActiveFilterPanel = !this.isActiveFilterPanel;
  }

  applyFilterEvent() {
    this.filterModels = this.createFilterModels(this.filterForm);
    this.loadCategories();
  }

  resetFilterEvent() {
    this.filterForm.name = "";
    this.filterForm.id = undefined;
    this.filterForm.status = undefined;

    this.filterModels = [];
    this.loadCategories();
  }

  createEvent() {
    this.dialog.open(CreateCategoryComponent, {
      height: '450px',
      width: '1000px',
      position: {right: '20%', left: '22%', top: '5%'},
      panelClass: 'rounded-lg'
    })
  }

  updateEvent(category: CategoryBadge) {
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

  deleteEvent(category: CategoryBadge) {
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

  // Private Methods

  ngOnInit(): void {
    this.loadCategories();
    this.stateActionRegistry.add(new StateActionNode(CATEGORY_KEY, this));
  }

  private createFilterModels(filterCategoryForm: CategoryForm): FilterModel[] {
    let filterModels: FilterModel[] = []

    if (filterCategoryForm.id != undefined) {
      filterModels.push(new FilterModel('id.equals', FieldType.TEXT, filterCategoryForm.id));
    }

    if (filterCategoryForm.name.trim().length != 0) {
      filterModels.push(new FilterModel('name.contains', FieldType.TEXT, filterCategoryForm.name));
    }

    if (filterCategoryForm.status != undefined) {
      filterModels.push(new FilterModel('status.equals', FieldType.TEXT, filterCategoryForm.status));
    }
    return filterModels;
  }

  private loadCategories() {
    console.log('Filter Models ' + this.filterModels.length);

    // load data
    this.categoryService.getByQueryPagination({
      sort: this.sort,
      size: this.pageSize,
      page: this.currentPage,
      filterModels: this.filterModels
    }).subscribe(response => {
      if (response.ok) {
        this.categories = this.categoryService.mapCategoriesToCategoriesBadge(response.body!);
      }
    })

    // data count
    this.categoryService.countByQuery(this.filterModels)
      .subscribe(response => {
        if (response.ok) {
          console.log('total count ' + response.body);
          this.totalPageSize = response.body!;
        }
      })
  }
}
