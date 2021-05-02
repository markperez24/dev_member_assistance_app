import { Component, OnInit, ViewChild } from '@angular/core';
import { HospitalService } from '../../shared/services/hospital.service';
import { DialogService } from '../../shared/services/dialog.service';
import { CommonService } from '../../shared/services/common.service';
import { MemberAssistanceApi } from '../../shared/constants/member.assistance.api';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { MemberAssistanceMessages } from '../../shared/constants/member.assistance.messages';
import { NgxSpinnerService } from 'ngx-spinner';
import { processing } from '../../operators';
import { MemberAssistanceConstants } from '../../shared/constants/member.assistance.constants';

import * as _ from 'lodash';

@Component({
  selector: 'app-hospital',
  templateUrl: './hospital.component.html',
  styleUrls: ['./hospital.component.scss']
})
export class HospitalComponent implements OnInit {
  displayedColumns: string [] = ['hospitalName', 'location', 'budget', 'balance'];
  dataSource = new MatTableDataSource<any>([]);
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  
  access: boolean;
  totalBudget: any;
  totalBalance: any;
  totalAssistedCount: any;
  isLoading: any;
  searchField: FormControl;
  searchTextChanged = new Subject<string>();

  constructor(private hospitalService: HospitalService,
              private commonService: CommonService,
              private spinnerService: NgxSpinnerService,
              private dialogService: DialogService,
              private router: Router) {
    this.isLoading = false;
    this.searchField = new FormControl();
    this.access = false;
  }

  ngOnInit(): void {
    //this.access = this.hospitalService.getHospitalAccess();
    this.access = this.hospitalService.getAddHospitalAccess();
    this.paginator.pageSizeOptions = [5, 10, 20];
    this.paginator.pageSize = 20;
    this.dataSource.paginator = this.paginator;
    this.isLoading = true;
    this.searchField.valueChanges
      .pipe(
        debounceTime(MemberAssistanceConstants.SEARCH_DELAY),
        distinctUntilChanged()
      ).subscribe(searched => {
        this.paginator.pageIndex = 0;
        this.displayHospitals(searched);
    });
    this.displayHospitals(this.searchField.value);
  }

  displayHospitals(searched: string) {
    this.hospitalService.displayHospitals(
      MemberAssistanceApi.GET_HOSPITALS, {
        searchField: searched,
        pageIndex: this.paginator.pageIndex,
        pageSize: this.paginator.pageSize
      }).pipe(processing(this.spinnerService))
      .subscribe(result => {
      this.isLoading = false;
      if (result && result.success) {
        console.log('Hospitals: ', result);
        this.totalBudget = result.summary.totalBudget;
        this.totalBalance = result.summary.totalBalance;
        this.totalAssistedCount = result.summary.totalAssistedCount;
        this.dataSource.data = result.hospitals;
        this.dataSource.paginator = this.paginator;
      } else if (result.error) {
        this.dialogService.open(
          {
            title: MemberAssistanceMessages.TITLE_WARNING_DISPLAY_HOSPITALS,
            message: result.message
          }
        );
      }
      
    });
  }

  addHospitalDialog(): void {
    const addHospitalDialogRef = this.dialogService.openAddHospitalDialog({});
    this.displayHospitals(this.searchField.value);
  }

}
