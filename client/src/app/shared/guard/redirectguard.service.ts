import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { Observable } from 'rxjs';

@Injectable()
export class RedirectGuard implements CanActivate {
  auth;

  constructor(private keycloakService: KeycloakService, private router: Router) {
    this.auth =  keycloakService;
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    //Your redirection logic goes here
    if (this.auth.isLoggedIn()) {
      console.log('Roles', this.auth.getUserRoles());
      if (this.auth.getUserRoles().indexOf('Member') > -1) {
        this.router.navigate(['/my-profile']);
      }

      if (this.auth.getUserRoles().indexOf('Administrator') > -1) {
        this.router.navigate(['/members']);
      }
    }
    return false;
  }
}
