import { Injectable } from '@angular/core';
import { AbstractService } from './abstract.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommonService } from './common.service';

@Injectable({providedIn: 'root'})
export class MedicalAssistanceService extends AbstractService {

  constructor(private http: HttpClient,
              private commonService: CommonService) {
    super();
  }

  /**
   * Display hospital lists.
   * @param strUrls
   * @param options
   */
  getList(strUrl: string, options: any): Observable<any> {
    return this.commonService.get(strUrl, options);
  }

  saveApplication(strUrl: string, options: {}) {
      return this.commonService.post(strUrl, options);
  }

  getVoucherImage(strUrl: String, params: any) {
    return this.commonService.getNoParams(strUrl.concat(params.accountNumber),
      {responseType: 'blob' as 'json'});
  }

  claimVoucher(strUrl : string , options: any) {
    return this.commonService.post(strUrl, options);
  }
}
