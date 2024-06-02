import {Observable} from "rxjs";
import {BaseService} from "./base.service";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {MicroserviceConfigService} from "./microservice.config.service";
import {FilterModel} from "../models/filter/filter.model";
import {FilterService} from "./filter.service";
import {QueryCriteria} from "../models/filter/query.criteria";

/**
 * @author Nurislom
 * @package app.core
 */
export abstract class AbstractService<E, ID> implements BaseService<E, ID> {

  protected constructor(
    protected httpClient: HttpClient,
    protected filterService: FilterService,
    protected microserviceConfig: MicroserviceConfigService
  ) {
  }

  public getByQuery(filterModels: FilterModel[]): Observable<HttpResponse<E[]>> {
    let url = this.filterService.filterModelJoinUrl(this.getEndpoint(), filterModels);
    return this.httpClient.get<E[]>(url, {observe: 'response'});
  }

  public countByQuery(filterModels: FilterModel[]): Observable<HttpResponse<number>> {
    let endpoint = this.getEndpoint();
    let url = this.filterService.filterModelJoinUrl(`${endpoint}/count`, filterModels);
    return this.httpClient.get<number>(url, {observe: 'response'});
  }

  public getByQueryPagination(queryCriteria: QueryCriteria): Observable<HttpResponse<E[]>> {
    let url = this.filterService.filterModelJoinUrl(this.getEndpoint(), queryCriteria.filterModels!);
    let paginationUrl = this.joinPagination(url, queryCriteria.size, queryCriteria.page, queryCriteria.sort);
    return this.httpClient.get<E[]>(paginationUrl, {observe: 'response'});
  }

  private joinPagination(url: string, size: number, page: number, sort?: string): string {
    return `${url.includes('?') ? `${url}&` : `${url}?` }size=${size}&page=${page - 1}${ sort !== null ? `&sort=${sort}` : '' }`;
  }

  public getAll(queryParams: string): Observable<HttpResponse<E[]>> {
    let endpoint = this.getEndpoint();
    if (queryParams != null) {
      endpoint = endpoint + '?' + queryParams;
    }
    return this.httpClient.get<E[]>(endpoint, {observe: 'response'});
  }

  public getById(id: ID): Observable<HttpResponse<E>> {
    let endpoint = this.getEndpoint();
    return this.httpClient.get<E>(`${endpoint}/${id}`, {observe: 'response'});
  }

  public create(body: any): Observable<HttpResponse<E>> {
    let endpoint = this.getEndpoint();
    return this.httpClient.post<E>(endpoint, body, {observe: 'response'});
  }

  public update(body: any, id: ID): Observable<HttpResponse<E>> {
    let endpoint = this.getEndpoint();
    return this.httpClient.put<E>(`${endpoint}/${id}`, body, {observe: 'response'});
  }

  public partialUpdate(body: any, id: ID): Observable<HttpResponse<E>> {
    let endpoint = this.getEndpoint();
    return this.httpClient.patch<E>(`${endpoint}/${id}`, body, {observe: 'response'});
  }

  public delete(id: ID): Observable<HttpResponse<{}>> {
    let endpoint = this.getEndpoint();
    return this.httpClient.delete<{}>(`${endpoint}/${id}`, {observe: 'response'});
  }

  public count(): Observable<HttpResponse<number>> {
    let endpoint = this.getEndpoint();
    return this.httpClient.get<number>(`${endpoint}/count`, {observe: 'response'});
  }

  private getEndpoint() {
    return this.microserviceConfig.getEndpointFor(this.getBaseUrl(), this.getMicroservice());
  }

  /**
   *
   */
  public abstract getBaseUrl(): string;

  /**
   *
   */
  public abstract getMicroservice(): string;
}
