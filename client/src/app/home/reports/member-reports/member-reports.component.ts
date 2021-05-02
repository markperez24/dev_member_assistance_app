import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { each } from 'lodash';
import { catchError, debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { ReportService } from '../../../shared/services/reports.service';
import { MemberAssistanceApi } from '../../../shared/constants/member.assistance.api';
import { MatTableDataSource } from '@angular/material/table';
import { MemberReportsModel } from '../../../shared/model/member-reports.model';
import { MatDialogRef } from '@angular/material/dialog';
import { ReportTypeSelectionDialogComponent } from '../report-type-selection-dialog/report-type-selection-dialog.component';
import { DialogService } from '../../../shared/services/dialog.service';
import { MemberAssistanceMessages } from '../../../shared/constants/member.assistance.messages';
import { processing } from '../../../operators';
import { NgxSpinnerService } from 'ngx-spinner';
import { MemberAssistanceConstants } from '../../../shared/constants/member.assistance.constants';
import * as moment from 'moment';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-member-reports',
  templateUrl: './member-reports.component.html',
  styleUrls: ['./member-reports.component.scss']
})
export class MemberReportsComponent implements OnInit {
  processing: boolean;
  members!: MemberReportsModel[];

  sName: FormControl;
  sLocation: FormControl;
  sGender: FormControl;
  sDateOfBirth: FormControl;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string [] = ['fullName', 'location', 'dateOfBirth', 'gender'];
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  dialogRef: MatDialogRef<ReportTypeSelectionDialogComponent> | undefined;

  constructor(private reportService: ReportService,
              private dialogService: DialogService,
              private spinnerService: NgxSpinnerService) {
    this.processing = false;
    this.sName = new FormControl();
    this.sGender = new FormControl();
    this.sLocation = new FormControl();
    this.sDateOfBirth = new FormControl();
  }

  ngOnInit(): void {
    this.paginator.pageSizeOptions = [10, 20, 50];
    this.paginator.pageSize = 20;
    this.dataSource.paginator = this.paginator;
    this.displayMembers();
    this.initSearchFields();
  }

  private displayMembers() {
    this.processing = true;
    this.reportService.getMembers(
      MemberAssistanceApi.GET_REPORTS_MEMBERS,
      {
        sName: this.sName.value,
        sLocation: this.sLocation.value,
        sGender: this.sGender.value,
        sDateOfBirth: this.formatDate(this.sDateOfBirth.value),
        pageIndex: this.paginator.pageIndex,
        pageSize: this.paginator.pageSize
      }
    )
      .pipe(processing(this.spinnerService))
      .subscribe(res => {
      if (res && res.success) {
        this.members = res.members;
        this.paginator.length = res.total;
        this.dataSource = new MatTableDataSource<MemberReportsModel>(this.members);
      }
    });
  }

  private initSearchFields() {
    let searchFields = [this.sName, this.sLocation, this.sGender, this.sDateOfBirth];
    each(searchFields, i => {
      i.valueChanges
        .pipe(
          debounceTime(MemberAssistanceConstants.SEARCH_DELAY),
          distinctUntilChanged()
        ).subscribe(searched => {
        i.setValue(searched);
        this.paginator.pageIndex = 0;
        this.displayMembers();
      });
    });
  }

  onChangePage($event: PageEvent) {
    this.paginator.pageIndex = $event.pageIndex;
    this.paginator.pageSize = $event.pageSize;
    this.displayMembers();
  }

  generateReport() {
    this.reportService.openTypeSelectDialog(                                                                                                                                                                                    )
      .subscribe(res => {
        if(res && res.type) {
          this.reportService.generateMemberReports(
            MemberAssistanceApi.POST_MEMBER_GENERATE_REPORTS,
            {
              type: res.type,
              sName: this.sName.value,
              sLocation: this.sLocation.value,
              sGender: this.sGender.value,
              sDateOfBirth: this.formatDate(this.sDateOfBirth.value)
            }
          ).pipe(
             processing(this.spinnerService),
             catchError(this.parseErrorBlob)
          )
            .subscribe(res => {
            if (res && res.success) {
              this.dialogService.open({
                title: MemberAssistanceMessages.TITLE_SUCCESS_MEMBER_REPORTS_GENERATED,
                message: MemberAssistanceMessages.MSG_SUCCESS_MEMBER_REPORTS_GENERATED
              });
            }
          })
        }
      })
  }

  downloadReport() {
    this.reportService.openTypeSelectDialog()
      .subscribe(res => {
        if(res && res.type) {
          this.reportService.downloadMemberReports(
            MemberAssistanceApi.GET_MEMBER_GENERATE_REPORTS,
            {
              type: res.type
            }
          ).pipe(
            processing(this.spinnerService)
           // catchError(this.parseErrorBlob)
          )
            .subscribe(res => {
              if(res && !res.error) {
                const file = new Blob([res], {type: 'application/octet-stream'});
                const link = document.createElement('a');
                link.href = window.URL.createObjectURL(file);
                link.download = MemberAssistanceConstants.MEMBER_REPORTS_FILE_PREFIX
                  .concat(moment(new Date()).format('YYYY-MM-DD'))
                  .concat(MemberAssistanceConstants.EXCEL_FILE_SUFFIX);
                link.click();
              }
            }/*, error => {
              const message = JSON.parse(error.error.text()).message;
            }*/);
        }
      })
  }

  parseErrorBlob(obsvr: any): Observable<any> {
    return obsvr;
  }


  private formatDate(val: string) {
    if(val != null) {
      return moment(val).format('MM/DD/YYYY');
    }
    return val;
  }
}
