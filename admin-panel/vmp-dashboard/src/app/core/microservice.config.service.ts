import {Injectable, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {GATEWAY, GATEWAY_URL} from "./global.constants";
import {load} from "@angular-devkit/build-angular/src/utils/server-rendering/esm-in-memory-loader/loader-hooks";

@Injectable({
  providedIn: 'root'
})
export class MicroserviceConfigService implements OnInit {

  private config!: any;

  getConfig(key: any): string {
    return this.config[key];
  }

  getEndpointFor(api: string, microservice?: string): string {
    if (microservice) {
      return `${GATEWAY_URL}/services/${microservice}/${api}`;
    }
    return `${GATEWAY_URL}${api}`;
  }

  ngOnInit(): void {
    this.load();
  }

  public load() {
    fetch('application-config.json')
      .then(res => res.json())
      .then(config => {
        this.config = config;
      })
  }
}
