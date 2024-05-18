import {Injectable} from "@angular/core";
import {AbstractService} from "../../../../core/abstract.service";
import {Category} from "../../../../models/category";
import {HttpClient} from "@angular/common/http";
import {MicroserviceConfigService} from "../../../../core/microservice.config.service";

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends AbstractService<Category, number> {

  constructor(
    httpClient: HttpClient,
    microserviceConfig: MicroserviceConfigService
  ) {
    super(httpClient, microserviceConfig);
  }

  public override getBaseUrl(): string {
    return 'api/categories';
  }

  public override getMicroservice(): string {
    return 'productms'
  }
}
