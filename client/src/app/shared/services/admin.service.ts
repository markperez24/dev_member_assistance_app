import { Injectable } from "@angular/core";
import { AbstractService } from "./abstract.service";
import { CommonService } from "./common.service";

@Injectable({providedIn: 'root'})
export class AdminService extends AbstractService {

  constructor(private commonService: CommonService) {
    super();
  }

  getApplication(strUrl: string, options: any) {
    return this.commonService.get(strUrl, options);
  }

  approveApplication(strUrl: string, options:any) {
    return this.commonService.post(strUrl, options);
  }
}
