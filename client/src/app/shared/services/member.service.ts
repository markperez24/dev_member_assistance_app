import { Injectable } from '@angular/core';
import { AbstractService } from './abstract.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { CommonService } from './common.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DenyDialogComponent } from '../../home/members/deny-dialog/deny-dialog.component';
import { MemberListingModel } from '../model/member-listing.model';
import { ApproveDialogComponent } from '../../home/members/approve-dialog/approve-dialog.component';

@Injectable({providedIn: 'root'})
export class MemberService extends AbstractService {

  denyDialogRef: MatDialogRef<DenyDialogComponent> | undefined;
  approveDialogRef: MatDialogRef<ApproveDialogComponent> | undefined;

  constructor(private http: HttpClient,
              private dialog: MatDialog,
              private commonService: CommonService) {
    super();
  }

  /**
   * Display member list.
   * @param strUrl
   * @param options
   */
  displayMembers(strUrl: string, options: any): Observable<any> {
    return this.commonService.get(strUrl, options);
  }

  openDenyDialog() {
    this.denyDialogRef = this.dialog.open(DenyDialogComponent, {
      width: '400px'
    })
  }

  closeDenyDialog(): Observable<any> {
    if (this.denyDialogRef) {
      return this.denyDialogRef.afterClosed()
        .pipe(take(1),
          map(res => {
              return res;
            }
          ));
    } else {
      return new Observable<any>();
    }
  }

  openApproveDialog(member: MemberListingModel) {
    this.approveDialogRef = this.dialog.open(ApproveDialogComponent, {
      data: {member: member},
      width: '350px'
    })
  }

  closeApproveDialog(): Observable<any> {
    if (this.approveDialogRef) {
      return this.approveDialogRef.afterClosed()
        .pipe(take(1),
          map(res => {
              return res;
            }
          ));
    } else {
      return new Observable<any>();
    }
  }

  denyApplication(strUrl: string, options: any): Observable<any> {
    return this.commonService.post(strUrl, options);
  }

  approveApplication(strUrl: string, options: any): Observable<any> {
    return this.commonService.post(strUrl, options);
  }

  displayMemberDetails(strUrl: string, options: any): Observable<any> {
    return this.commonService.get(strUrl, options);
  }

  getImage(strUrl: String, params: any) {
      return this.commonService.getNoParams(strUrl.concat(params.accountNumber),
        {responseType: 'blob' as 'json'});
  }
}
