import { Injectable } from '@angular/core';
import { AbstractService } from './abstract.service';
import { CommonService } from './common.service';

@Injectable({providedIn: 'root'})
export class UtilService extends AbstractService {

  constructor(private commonService: CommonService) {
    super();
  }

  public formatMobile(number: string) {
    let f_val = number.replace(/\D/g, "");
    let formattedMobile = f_val.slice(0, 4) + "-" + f_val.slice(4, 7) + "-" + f_val.slice(7,11);
    return formattedMobile;
  }
}
