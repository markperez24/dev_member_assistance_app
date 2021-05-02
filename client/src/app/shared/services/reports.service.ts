import { Injectable } from '@angular/core';
import { CommonService } from './common.service';
import { Observable } from 'rxjs';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ReportTypeSelectionDialogComponent } from '../../home/reports/report-type-selection-dialog/report-type-selection-dialog.component';
import { map, take } from "rxjs/operators";

@Injectable({providedIn: 'root'})
export class ReportService {

  dialogRef: MatDialogRef<ReportTypeSelectionDialogComponent> | undefined;

  constructor(private commonService: CommonService,
              private dialog: MatDialog) {
  }

  getMembers(strUrl: string, options: any): Observable<any>{
    return this.commonService.get(strUrl, options);
  }

  openTypeSelectDialog(): Observable<any> {
    this.dialogRef = this.dialog.open(ReportTypeSelectionDialogComponent, {
      width: '300px'
    });
    if (this.dialogRef) {
      return this.dialogRef.afterClosed()
        .pipe(take(1),
          map(res => {
              return res;
            }
          ));
    } else {
      return new Observable<any>();
    }
  }

  generateMemberReports(strUrl: string, options : any) {
    return this.commonService.post(strUrl, options);
  }

  downloadMemberReports(strUrl: string, params : any) {
    return this.commonService.getNoParams(strUrl.concat(params.type),
      {responseType: 'blob'});
  }

  getHospitals(strUrl: string, options: any): Observable<any>{
    return this.commonService.get(strUrl, options);
  }

  generateHospitalReports(strUrl: string, options : any) {
    return this.commonService.post(strUrl, options);
  }

  downloadHospitalReports(strUrl: string, params : any) {
    return this.commonService.getNoParams(strUrl.concat(params.type),
      {responseType: 'blob'});

  }
}
