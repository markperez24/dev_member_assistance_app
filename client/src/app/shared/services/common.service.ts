import { Injectable } from '@angular/core';
import { AbstractService } from './abstract.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { MemberAssistanceConstants } from '../constants/member.assistance.constants';
import { RoleAccessActionConstants } from '../constants/role-access.constants';
import { Observable, Subject } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({providedIn: 'root'})
export class CommonService extends AbstractService {

  private processing: Subject<boolean>;

  constructor(
    private http: HttpClient
  ) {
    super();
    this.processing = new Subject();
  }

  /**
   * Get method with request parameter options.
   * @param strUrl
   * @param options
   */
  get(strUrl: string, options: any): Observable<any> {
    let params = new HttpParams()

    for (const key in options) {
      params = options[key] != null ? params.set(key, options[key]) : params;
    }

    return this.http
      .get(this.getApiUrl().concat(strUrl), {params})
      .pipe(
        catchError(this.handleError)
      );
  }

  getNoParams(strUrl: string, options: any): Observable<any> {
    return this.http
      .get(this.getApiUrl().concat(strUrl), options)
      .pipe(
        catchError(this.handleError)
      )
  }

  /**
   * Post method api
   * @param strUrl
   * @param params
   */
  post(strUrl: string, params: any): Observable<any> {
    return this.http
      .post(this.getApiUrl().concat(strUrl), params)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Access for navigation controls
   */
  getActionAccess() {
    let access = {
      members: RoleAccessActionConstants.nav_members,
      reports: RoleAccessActionConstants.nav_members,
      hospitals: RoleAccessActionConstants.nav_members,
      applyMedicalAssistance: RoleAccessActionConstants.nav_assistance_application,
      myProfile: RoleAccessActionConstants.nav_my_profile
    };
    return access;
  }


  /**
   * Navigation displays
   */
  getNavDisplay(userRoles: any) {
    let appDisplayName = MemberAssistanceConstants.APP_NAME;
    if (userRoles.indexOf('Member') > -1) {
      appDisplayName = MemberAssistanceConstants.MEMBER_APP_DISPLAY_NAME;
    } else if (userRoles.indexOf('Administrator') > -1) {
      appDisplayName = MemberAssistanceConstants.ADMIN_APP_DISPLAY_NAME;
    }
    return appDisplayName;
  }

  isProcessing() {
    return this.processing;
  }

  setProcessing(isBoolean: boolean) {
    this.isProcessing().next(isBoolean);
  }
}
