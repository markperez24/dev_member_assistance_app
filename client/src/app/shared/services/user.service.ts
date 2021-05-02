import { Injectable } from '@angular/core';
import { AbstractService } from './abstract.service';
import { Observable } from 'rxjs';
import { CommonService } from './common.service';

@Injectable({providedIn: 'root'})
export class UserService extends AbstractService {

  constructor(private commonService: CommonService) {
    super();
  }

  /**
   * Get video details to load and play.
   * @param strUrl
   * @param options
   */
  register(strUrl: string, options: any): Observable<any> {
    return this.commonService.post(strUrl, options);
  }

  verifyIfExists(strUrl: string, email: string): Observable<any> {
    return this.commonService.getNoParams(strUrl.concat(email), {});
  }
}
