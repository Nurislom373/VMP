import {Injectable, OnInit} from "@angular/core";
import {CategoryStatus, CategoryStatusBadge} from "../models/category";

@Injectable({
  providedIn: 'root'
})
export class CategoryStatusBadgeService {

  private categoryStatusBadges: CategoryStatusBadge[] = [
    {
      status: CategoryStatus.ACTIVE,
      badgeColor: 'green'
    },
    {
      status: CategoryStatus.DELETED,
      badgeColor: 'red'
    },
    {
      status: CategoryStatus.BLOCKED,
      badgeColor: 'gray'
    }
  ]

  public getCategoryStatusBadges(): CategoryStatusBadge[] {
    return this.categoryStatusBadges;
  }
}
