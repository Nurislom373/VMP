import {Injectable} from "@angular/core";
import {AbstractService} from "../../../../core/abstract.service";
import {Category, CategoryBadge, CategoryStatus} from "../../../../models/category";
import {HttpClient} from "@angular/common/http";
import {MicroserviceConfigService} from "../../../../core/microservice.config.service";
import {CategoryStatusBadgeService} from "./category.status.badge.service";
import {FilterService} from "../../../../core/filter.service";

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends AbstractService<Category, number> {

  constructor(
    httpClient: HttpClient,
    filterService: FilterService,
    microserviceConfig: MicroserviceConfigService,
    private categoryStatusBadgeService: CategoryStatusBadgeService
  ) {
    super(httpClient, filterService, microserviceConfig);
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

  public override getBaseUrl(): string {
    return 'api/categories';
  }

  public override getMicroservice(): string {
    return 'productms'
  }
}
