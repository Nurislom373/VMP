import {Component, ViewChild} from "@angular/core";
import {FormsModule, NgForm} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {CategoryService} from "../service/category.service";
import {FilterModel} from "../../../../models/filter/filter.model";
import {FieldType} from "../../../../models/filter/field.type";
import {CategoryForm} from "../../../../models/form/category.form";
import {StateActionRegistry} from "../../../../services/state.action.registry";
import {CATEGORY_KEY} from "../../../../core/global.constants";

@Component({
  selector: 'category-filter',
  standalone: true,
  templateUrl: './category.filter.component.html',
  imports: [
    FormsModule,
    NgForOf
  ],
  styleUrl: './category.filter.component.css'
})
export class CategoryFilterComponent {

  protected filterCategoryForm: CategoryForm = new CategoryForm();

  @ViewChild("categoryForm")
  CategoryForm!: NgForm;

  constructor(
    private stateActionRegistry: StateActionRegistry,
    private categoryService: CategoryService
  ) {
  }

  getCategoryStatuses() {
    return this.categoryService.getCategoryStatuses()
  }

  applyFilter() {
    let filterModels = this.createFilterModels(this.filterCategoryForm);
    let stateAction = this.stateActionRegistry.get(CATEGORY_KEY);

    stateAction.action.changeProperties(filterModels);
    this.closeModal();
  }

  resetFilter() {
    this.filterCategoryForm.name = "";
    this.filterCategoryForm.status = undefined;

    let stateAction = this.stateActionRegistry.get(CATEGORY_KEY);
    stateAction.action.changeProperties([]);
    this.closeModal();
  }

  closeModal() {
    let htmlElement = document.getElementById('filter-popover');
    let classList = htmlElement?.classList;

    if (classList?.contains('hidden')) {
      classList.remove('hidden');
      return;
    }
    classList?.add('hidden');
  }

  private createFilterModels(filterCategoryForm: CategoryForm): FilterModel[] {
    let filterModels: FilterModel[] = []

    if (filterCategoryForm.name.trim().length != 0) {
      filterModels.push(new FilterModel('name.contains', FieldType.TEXT, filterCategoryForm.name));
    }

    if (filterCategoryForm.status != undefined) {
      filterModels.push(new FilterModel('status.equals', FieldType.TEXT, filterCategoryForm.status));
    }
    return filterModels;
  }
}
