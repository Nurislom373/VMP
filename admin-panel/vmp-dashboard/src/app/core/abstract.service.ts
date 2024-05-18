import {Observable} from "rxjs";
import {BaseService} from "./base.service";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {MicroserviceConfigService} from "./microservice.config.service";

/**
 * @author Nurislom
 * @package app.core
 */
export abstract class AbstractService<E, ID> implements BaseService<E, ID> {

  protected constructor(
    protected httpClient: HttpClient,
    protected microserviceConfig: MicroserviceConfigService
  ) {
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
