import { Injectable } from '@angular/core';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class AuthGuard extends KeycloakAuthGuard {
  constructor(protected router: Router, protected keycloakAngular: KeycloakService) {
    super(router, keycloakAngular);
  }

  isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    return new Promise(async (resolve, reject) => {
      if (!this.authenticated) {
        this.keycloakAngular.login({
          redirectUri: window.location.origin + '/home'
        });
        return;
      }
      const requiredRoles = route.data.roles;
      let granted: boolean = false;
      if (!requiredRoles || requiredRoles.length === 0) {
        granted = true;
      } else {
        for (const requiredRole of requiredRoles) {
          if (this.roles.indexOf(requiredRole) > -1) {
            granted = true;
            localStorage.setItem('isLoggedIn', "true");
            break;
          }
        }
      }
      if (state.url === '/home') {
        if (this.keycloakAngular.getUserRoles().indexOf('Member') > -1) {
          await this.router.navigate(['/my-profile']);
        }
        if (this.keycloakAngular.getUserRoles().indexOf('Administrator') > -1) {
          await this.router.navigate(['/members']);
        }
      }

      if (granted === false) {
        this.router.navigate(['/public']);
      }
      resolve(granted);
    });
  }
}
