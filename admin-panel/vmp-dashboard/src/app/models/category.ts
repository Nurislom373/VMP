export interface Category {
  id?: number
  name: String
  status?: CategoryStatus
}

export interface CategoryBadge extends Category {
  statusBadge?: CategoryStatusBadge
}

export interface CategoryStatusBadge {
  status: CategoryStatus,
  badgeColor: string
}

export enum CategoryStatus {
  ACTIVE = "ACTIVE",
  DELETED = "DELETED",
  BLOCKED = "BLOCKED",
  UNKNOWN = "UNKNOWN"
}
