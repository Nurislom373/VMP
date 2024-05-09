
export interface Category {
  id?: number
  name: String
  status?: CategoryStatus
}

export enum CategoryStatus {
  ACTIVE = "ACTIVE",
  DELETED = "DELETED",
  BLOCKED = "BLOCKED"
}
