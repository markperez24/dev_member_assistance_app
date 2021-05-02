import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { KeycloakService } from 'keycloak-angular';
import { AbstractService } from './abstract.service';

@Injectable({providedIn: 'root'})
export class RoleService extends AbstractService{

  /**
   * Creates a new SalesOrderService with the injected HttpClient.
   * @param {HttpClient} http - The injected HttpClient.
   * @constructor
   */
  constructor(private http: HttpClient,
              private keycloakService: KeycloakService) {
    super();
  }

  hasAccess(accessRoles: string[]): boolean {
    for (const accessRole of accessRoles) {
      //if (_.includes(this.keycloakService.getUserRoles(), accessRole)) {
      if(this.keycloakService.getUserRoles().indexOf(accessRole) > -1) {
        return true;
      }
    }
    return false;
  }
}
