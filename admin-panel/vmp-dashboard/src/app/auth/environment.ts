export const environment = {
  production: false,
  keycloak: {
    authority: 'http://localhost:9080',
    redirectUri: 'http://localhost:4200',
    postLogoutRedirectUri: 'http://localhost:4200',
    realm: 'jhipster',
    clientId: 'web_app',
  },
  idleConfig: { idle: 10, timeout: 60, ping: 10 },
};
