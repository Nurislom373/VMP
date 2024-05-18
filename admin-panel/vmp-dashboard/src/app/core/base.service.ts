import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";

export interface BaseService<E, ID> {

  /**
   *
   */
  getAll(queryParams: string): Observable<HttpResponse<E[]>>;

  /**
   *
   * @param id
   */
  getById(id: ID): Observable<HttpResponse<E>>;

  /**
   *
   * @param body
   */
  create(body: any): Observable<HttpResponse<E>>;

  /**
   *
   * @param body
   * @param id
   */
  update(body: any, id: ID): Observable<HttpResponse<E>>;

  /**
   *
   * @param body
   * @param id
   */
  partialUpdate(body: any, id: ID): Observable<HttpResponse<E>>;

  /**
   *
   * @param id
   */
  delete(id: ID): Observable<HttpResponse<{}>>;

  /**
   *
   */
  count(): Observable<HttpResponse<number>>;
}
