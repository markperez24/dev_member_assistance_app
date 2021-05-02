import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from "@angular/forms";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { each } from 'lodash';
import { debounceTime, distinctUntilChanged, map, take } from "rxjs/operators";
import { ReportService } from "../../../shared/services/reports.service";
import { MemberAssistanceApi } from "../../../shared/constants/member.assistance.api";
import { MatTableDataSource } from "@angular/material/table";
import { HospitalReportsModel } from "../../../shared/model/hospital-reports.model";
import { MatDialogRef } from "@angular/material/dialog";
import { ReportTypeSelectionDialogComponent } from "../report-type-selection-dialog/report-type-selection-dialog.component";
import { DialogService } from "../../../shared/services/dialog.service";
import { MemberAssistanceMessages } from "../../../shared/constants/member.assistance.messages";
import { processing } from "../../../operators";
import { NgxSpinnerService } from "ngx-spinner";
import { MemberAssistanceConstants } from "../../../shared/constants/member.assistance.constants";
import * as moment from 'moment';
import * as _ from 'lodash';

@Component({
  selector: 'app-hospital-reports',
  templateUrl: './hospital-reports.component.html',
  styleUrls: ['./hospital-reports.component.scss']
})
export class HospitalReportsComponent implements OnInit {
  processing: boolean;
  hospitals!: HospitalReportsModel[];

  sName: FormControl;
  sLocation: FormControl;
  sItem: FormControl;
  sDateFrom: FormControl;
  sDateTo: FormControl;
  sItemName: String;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string [] = ['hospitalName', 'location', 'dateAdded', 'item'];
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  dialogRef: MatDialogRef<ReportTypeSelectionDialogComponent> | undefined;

  constructor(private reportService: ReportService,
              private dialogService: DialogService,
              private spinnerService: NgxSpinnerService) {
    this.processing = false;
    this.sName = new FormControl();
    this.sItem = new FormControl();
    this.sLocation = new FormControl();
    this.sDateFrom = new FormControl();
    this.sDateTo = new FormControl();
    this.sItemName = new String();
  }

  ngOnInit(): void {
    this.paginator.pageSizeOptions = [10, 20, 50];
    this.paginator.pageSize = 20;
    this.dataSource.paginator = this.paginator;
    this.displayHospitals();
    this.initSearchFields();
  }

  private displayHospitals() {
    this.processing = true;
    this.reportService.getHospitals(
      MemberAssistanceApi.GET_REPORTS_HOSPITALS,
      {
        sName: this.sName.value,
        sLocation: this.sLocation.value,
        sItem: this.sItem.value,
        sDateFrom: this.formatDate(this.sDateFrom.value),
        sDateTo: this.formatDate(this.sDateTo.value),
        pageIndex: this.paginator.pageIndex,
        pageSize: this.paginator.pageSize
      }
    )
      .pipe(processing(this.spinnerService))
      .subscribe(res => {
      if (res && res.success) {
        this.sItemName = res.total > 0 ? res.hospitals[0].itemName : "Budget|Balance";
        this.hospitals = res.hospitals;
        this.paginator.length = res.total;
        this.dataSource = new MatTableDataSource<HospitalReportsModel>(this.hospitals);
      }
    });
  }

  private initSearchFields() {
    let searchFields = [this.sName, this.sLocation, this.sItem, this.sDateFrom, this.sDateTo];
    each(searchFields, i => {
      i.valueChanges
        .pipe(
          debounceTime(MemberAssistanceConstants.SEARCH_DELAY),
          distinctUntilChanged()
        ).subscribe(searched => {
        i.setValue(searched);
        this.paginator.pageIndex = 0;
        this.displayHospitals();
      });
    });
  }

  onChangePage($event: PageEvent) {
    this.paginator.pageIndex = $event.pageIndex;
    this.paginator.pageSize = $event.pageSize;
    this.displayHospitals();
  }

  generateReport() {
    this.reportService.openTypeSelectDialog()
      .subscribe(res => {
        if(res && res.type) {
          this.reportService.generateHospitalReports(
            MemberAssistanceApi.POST_HOSPITAL_GENERATE_REPORTS,
            {
              type: res.type,
              sName: this.sName.value,
              sLocation: this.sLocation.value,
              sItem: this.sItem.value,
              sDateFrom: this.formatDate(this.sDateFrom.value),
              sDateTo: this.formatDate(this.sDateTo.value),
            }
          ).pipe(processing(this.spinnerService))
            .subscribe(res => {
            if (res && res.success) {
              this.dialogService.open({
                title: MemberAssistanceMessages.TITLE_SUCCESS_HOSPITAL_REPORTS_GENERATED,
                message: MemberAssistanceMessages.MSG_SUCCESS_HOSPITAL_REPORTS_GENERATED
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
          this.reportService.downloadHospitalReports(
            MemberAssistanceApi.GET_HOSPITAL_GENERATE_REPORTS,
            {
              type: res.type
            }
          ).pipe(processing(this.spinnerService))
            .subscribe(res => {
              const file = new Blob([res], {type: 'application/octet-stream'});
              const link = document.createElement('a');
              link.href = window.URL.createObjectURL(file);
              link.download = MemberAssistanceConstants.HOSPITAL_REPORTS_FILE_PREFIX
                .concat(moment(new Date()).format("YYYY-MM-DD"))
                .concat(MemberAssistanceConstants.EXCEL_FILE_SUFFIX);
              link.click();
            })
        }
      })
  }

  private formatDate(val: string) {
    if(val != null) {
      return moment(val).format('MM/DD/YYYY');
    }
    return val;
  }
}
