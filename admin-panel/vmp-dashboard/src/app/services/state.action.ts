import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";

export interface StateAction {

  /**
   *
   */
  action(): void;

  /**
   *
   * @param property
   */
  changeProperties(property: any): void;
}
