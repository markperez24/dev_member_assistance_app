import { Injectable } from '@angular/core';
import { AbstractService } from './abstract.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommonService } from './common.service';
import { KeycloakService } from 'keycloak-angular';
import { MatDialog } from '@angular/material/dialog';
import { RoleAccessActionConstants } from '../constants/role-access.constants';

@Injectable({providedIn: 'root'})
export class HospitalService extends AbstractService {

  constructor(private http: HttpClient,
              private commonService: CommonService,
              private keycloakService: KeycloakService,
              private dialog: MatDialog) {
    super();
  }

  /**
   * Display hospital lists.
   * @param strUrls
   * @param options
   */
  displayHospitals(strUrl: string, options: any): Observable<any> {
    return this.commonService.get(strUrl, options);
  }

  /**
   * Get hospital details.
   * @param strUrls
   * @param options
   */
  getHospitalDetails(strUrl: string, options: any): Observable<any> {
    return this.commonService.get(strUrl, options);
  }

  /**
   * Register/add a new partner hospital
   * @param strUrl
   * @param options
   */
  register(strUrl: string, options: any): Observable<any> {
    return this.commonService.post(strUrl, options);
  }

  /**
   * Get hospital's medical assistance details
   * @param strUrl
   * @param options
   */
  viewHospitalMedicalAssistance(strUrl: string, options: any): Observable<any> {
    return this.commonService.get(strUrl, options);
  }

  /**
   * Add budget to the selected hospital
   * @param strUrl
   * @param options
   */
  saveAdditionalBudget(strUrl: string, options: {}) {
    return this.commonService.post(strUrl, options);
  }

  getHospitalAccess() {
    let access = {
      addHospital: RoleAccessActionConstants.hosp_add_hospital
    };
    return access;
  }

  getAddHospitalAccess() {
    if(this.keycloakService.getUserRoles().indexOf('Administrator') > -1) {
      return true;
    }
    return false;
  }

}
