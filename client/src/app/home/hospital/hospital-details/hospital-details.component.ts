import { Component, OnInit, ViewChild } from '@angular/core';
import { HospitalService } from '../../../shared/services/hospital.service';
import { HospitalDetailsService } from '../../../shared/services/hospital-details.service';
import { DialogService } from '../../../shared/services/dialog.service';
import { CommonService } from '../../../shared/services/common.service';
import { MemberAssistanceApi } from '../../../shared/constants/member.assistance.api';
import { ActivatedRoute } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { NgxSpinnerService } from 'ngx-spinner';
import { processing } from '../../../operators';

import * as _ from 'lodash';

@Component({
  selector: 'app-hospital-details',
  templateUrl: './hospital-details.component.html',
  styleUrls: ['./hospital-details.component.scss']
})
export class HospitalDetailsComponent implements OnInit {
  displayedColumns: string [] = ['patientName', 'memberId', 'voucherNumber', 'dateAwarded', 'amount'];
  dataSource = new MatTableDataSource<any>([]);
  // get reference to paginator
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  short = 'short';

  hasAddBudgetAccess: boolean;
  hasShowBudgetAccess: boolean;
  queryParam: any;
  hospitalId: any;
  hospitalName: any;
  location: any;
  contactPerson: any;
  designation: any;
  contactNumber: any;
  emailAddress: any;
  totalBudget: any;
  balance: any;
  totalAssistedCount: any;
  isLoading: boolean;
  resultsLength = 0;

  constructor(private hospitalService: HospitalService,
              private hospitalDetailsService: HospitalDetailsService,
              private commonService: CommonService,
              private spinnerService: NgxSpinnerService, 
              private dialogService: DialogService,
              private route: ActivatedRoute) {
    this.isLoading = true;
    this.hasAddBudgetAccess = false;
    this.hasShowBudgetAccess = false;
  }

  ngOnInit(): void {
    //this.access = this.commonService.getHospitalAccess();
    this.hasAddBudgetAccess = this.hospitalDetailsService.hasAddBudgetAccess();
    this.hasShowBudgetAccess = this.hospitalDetailsService.hasShowBudgetAccess();
    this.isLoading = true;
    this.route.queryParams.subscribe(params => {
      this.queryParam = params['hospital'];
    })
    this.paginator.pageSizeOptions = [5, 10, 20];
    this.paginator.pageSize = 20;
    this.dataSource.paginator = this.paginator;
    this.getHospitalDetails();
    this.getHospitalMedicalAssistanceList();
  }

  getHospitalDetails() {
    this.hospitalService.getHospitalDetails(
      MemberAssistanceApi.GET_HOSPITAL_DETAILS, {
        hospital : this.queryParam
      }).pipe(processing(this.spinnerService))
      .subscribe(result => {
      this.displayHospitalDetails(result);
      //this.isLoading = false;
    });
  }

  displayHospitalDetails(result: any) {
    this.hospitalId = result.hospital.hospitalId;
    this.hospitalName = result.hospital.hospitalName;
    this.location = result.hospital.location;
    this.contactPerson = result.hospital.contactPerson;
    this.designation = result.hospital.designation;
    this.contactNumber = result.hospital.contactNumber;
    this.emailAddress = result.hospital.emailAddress;

    this.totalBudget = result.hospital.budget;
    this.balance = result.hospital.balance;

  }

  getHospitalMedicalAssistanceList() {
    this.hospitalService.viewHospitalMedicalAssistance(
      MemberAssistanceApi.GET_HOSPITAL_MEDICAL_ASSISTANCE_DETAILS, {
        hospital : this.queryParam,
        pageIndex: this.paginator.pageIndex,
        pageSize: this.paginator.pageSize
      }).pipe(processing(this.spinnerService))
      .subscribe(result => {
      if (result && !result.error) {
        this.displayHospitalMedicalAssistance(result);
      } else {
        console.log(result.error);
      }
    });
  }

  displayHospitalMedicalAssistance(result : any) {
    this.totalAssistedCount = result.hospitalMedicalAssistance.totalAssistedCount;
    let medicalAssistanceList = result.hospitalMedicalAssistance.medicalAssistanceList;
    //this.resultsLength = medicalAssistanceList.length;
    medicalAssistanceList.forEach(function (i: any) {
      i.patientFullName = i.firstName.concat(' ').concat(i.middleName)
        .concat(' ').concat(i.lastName);
    });
    this.dataSource = new MatTableDataSource<any>(medicalAssistanceList);
    this.dataSource.paginator = this.paginator;
    this.isLoading = false;
  }

  addBudget() {
    //this.dialogService.openAddBudget(
    this.hospitalDetailsService.openAddBudget(
      {
        hospitalId: this.hospitalId,
        hospitalName: this.queryParam
      }
    );
  }

  showBudgetHistory() {
    //this.dialogService.openShowBudgetHistory(
    this.hospitalDetailsService.openShowBudgetHistory(
      {
        hospitalId: this.hospitalId,
        hospitalName: this.queryParam
      });
  }

}
