import {Injectable} from '@angular/core';
import {ApiService} from "./api.service";
import {Observable} from "rxjs";
import {Category} from "../models/category";

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  constructor(private apiServices: ApiService) {}

  getCategories = (url: string): Observable<Category[]> => {
    return this.apiServices.get(url, { responseType: 'json' });
  }
}
