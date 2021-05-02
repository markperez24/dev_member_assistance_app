import { Injectable } from '@angular/core';
import { AbstractService } from './abstract.service';
import { Observable } from 'rxjs';
import { CommonService } from './common.service';

@Injectable({providedIn: 'root'})
export class VerificationService extends AbstractService {

  constructor(private commonService: CommonService) {
    super();
  }

  init(strUrl: string, id: string): Observable<any> {
    return this.commonService.getNoParams(strUrl.concat(id), {});
  }
}
