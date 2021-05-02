import { Injectable } from '@angular/core';
import { AbstractService } from './abstract.service';
import { Observable } from 'rxjs';
import { CommonService } from './common.service';

@Injectable({providedIn: 'root'})
export class RegistrationService extends AbstractService {

  constructor(private commonService: CommonService) {
    super();
  }

  /**
   * Get video details to load and play.
   * @param strUrl
   * @param options
   */
  getVideoDetails(strUrl: string, options: any): Observable<any> {
    return this.commonService.get(strUrl, options);
  }

  verifyCaptcha(strUrl: string, params: any) {
    return this.commonService.post(strUrl, params);
  }
}
