import {Injectable} from '@angular/core';
import {ApiService} from "./api.service";
import {Observable} from "rxjs";
import {Category, CategoryBadge, CategoryStatus} from "../models/category";
import {CategoryStatusBadgeService} from "./category.status.badge.service";

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  constructor(private apiServices: ApiService, private categoryStatusBadgeService: CategoryStatusBadgeService) {
  }

  getCategoryStatuses() {
    return this.categoryStatusBadgeService.getCategoryStatusBadges();
  }

  mapCategoriesToCategoriesBadge = (categories: Category[]): CategoryBadge[] => {
    return categories.map(category => {
      return this.categoryToCategoryBadge(category)
    })
  }

  categoryToCategoryBadge(category: Category) {
    let categoryBadge = this.createCategoryBadge(category);
    this.setStatusBadge(categoryBadge);
    return categoryBadge;
  }

  private createCategoryBadge = ({id, name, status}: Category): CategoryBadge => ({id, name, status});

  private setStatusBadge(categoryBadge: CategoryBadge) {
    if (categoryBadge.status === null || undefined) {
      categoryBadge.statusBadge = {
        status: CategoryStatus.UNKNOWN,
        badgeColor: 'blue'
      }
      return;
    }
    this.getCategoryStatusBadges().forEach(statusBadge => {
      if (statusBadge.status === categoryBadge.status) {
        categoryBadge.statusBadge = statusBadge;
      }
    })
  }

  private getCategoryStatusBadges() {
    return this.categoryStatusBadgeService.getCategoryStatusBadges();
  }
}
