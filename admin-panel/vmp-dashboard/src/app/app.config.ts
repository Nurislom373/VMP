import {APP_INITIALIZER, ApplicationConfig, Provider} from '@angular/core';
import { provideRouter } from '@angular/router';

import { IconDefinition } from '@ant-design/icons-angular';
import { NzIconModule } from 'ng-zorro-antd/icon';

import { routes } from './app.routes';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from "@angular/common/http";
import {KeycloakBearerInterceptor, KeycloakService} from "keycloak-angular";
import {provideAnimations} from "@angular/platform-browser/animations";

import { PlusOutline } from '@ant-design/icons-angular/icons';

const icons: IconDefinition[] = [ PlusOutline ];

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:9080',
        realm: 'jhipster',
        clientId: 'web_app'
      },
      initOptions: {
        onLoad: 'check-sso'
      },
      bearerPrefix: 'Bearer',
      enableBearerInterceptor: true
    });
}

// Provider for Keycloak Bearer Interceptor
const KeycloakBearerInterceptorProvider: Provider = {
  provide: HTTP_INTERCEPTORS,
  useClass: KeycloakBearerInterceptor,
  multi: true
};

// Provider for Keycloak Initialization
const KeycloakInitializerProvider: Provider = {
  provide: APP_INITIALIZER,
  useFactory: initializeKeycloak,
  multi: true,
  deps: [KeycloakService]
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideAnimations(),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()), // Provides HttpClient with interceptors
    KeycloakService,
    KeycloakInitializerProvider,
    KeycloakBearerInterceptorProvider,
  ]
};
