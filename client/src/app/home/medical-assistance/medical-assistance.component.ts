import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MedicalAssistanceService } from '../../shared/services/medical-assistance.service';
import { MemberAssistanceApi } from '../../shared/constants/member.assistance.api';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { MemberService } from "../../shared/services/member.service";
import { DialogService } from "../../shared/services/dialog.service";
import { MemberAssistanceMessages } from "../../shared/constants/member.assistance.messages";

@Component({
  selector: 'app-medical-assistance',
  templateUrl: './medical-assistance.component.html',
  styleUrls: ['./medical-assistance.component.scss']
})
export class MedicalAssistanceComponent implements OnInit {
  isLoading: boolean;
  isAllowed: boolean;
  isDenied: boolean;
  isApproved: boolean;
  inReview: boolean;
  isProcessed: boolean;
  isClaimed: boolean;
  requirements: string[];
  resultsLength = 0;
  voucherImage: any;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string [] = ['hospitalName', 'name', 'amount', 'dateApplied', 'status'];
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  short = 'short';
  voucherNumber!: string;
  accountNumber!: string;
  mobile: boolean;

  constructor(private router: Router,
              private dialogService: DialogService,
              private memberService: MemberService,
              private medicalAssistanceService: MedicalAssistanceService) {
    this.isLoading = true;
    this.mobile = false;
    this.isAllowed = false;
    this.inReview = false;
    this.isDenied = false;
    this.isApproved = false;
    this.isProcessed = false;
    this.isClaimed = false;
    this.requirements = [];
  }

  ngOnInit(): void {
    this.isMobile();
    this.isLoading = true;
    this.paginator.pageSizeOptions = [5, 10, 20];
    this.paginator.pageSize = 5;
    this.dataSource.paginator = this.paginator;
    this.initMedicalAssistance();
  }

  initMedicalAssistance() {
    this.medicalAssistanceService.getList(
      MemberAssistanceApi.GET_MEDICAL_ASSISTANCE, {
        pageIndex: this.paginator.pageIndex,
        pageSize: this.paginator.pageSize
      }).subscribe(result => this.displayMedicalAssistance(result));
  }

  displayMedicalAssistance(result: any) {
    this.isAllowed = result.isAllowed;
    this.isDenied = result.isDenied;
    this.isApproved = result.isApproved;
    this.inReview = result.inReview;
    this.isProcessed = result.isProcessing;
    this.isClaimed = result.isClaimed;
    if(this.inReview) {
      this.requirements = result.requirements;
    }
    if(this.isApproved) {
      this.voucherNumber = result.voucherNumber;
      this.accountNumber = result.accountNumber;
      this.getImageVoucher();
    }
    let medicalAssistanceList = result.medicalAssistanceList;
    this.resultsLength = medicalAssistanceList.length;
    medicalAssistanceList.forEach(function (i: any) {
      i.patientFullName = i.firstName.concat(' ').concat(i.middleName)
        .concat(' ').concat(i.lastName);
    });
    this.dataSource = new MatTableDataSource<any>(medicalAssistanceList);
    this.dataSource.paginator = this.paginator;
    this.isLoading = false;
  }

  claimed() {
    this.medicalAssistanceService.claimVoucher(
      MemberAssistanceApi.POST_UPDATE_MEDICAL_ASSISTANCE_APPLICATION, {
      }).subscribe(result => {
        this.dialogService.open({
          title: MemberAssistanceMessages.TITLE_SUCCESS_VOUCHER_CLAIMED,
          message: MemberAssistanceMessages.MSG_SUCCESS_VOUCHER_CLAIMED
        });
        this.initMedicalAssistance();
    });
  }

  reload() {
    this.router.navigate(['./']);
  }

  private getImageVoucher() {
    this.medicalAssistanceService.getVoucherImage(
      MemberAssistanceApi.GET_MEDICAL_ASSISTANCE_VOUCHER_IMAGE, {
        accountNumber: this.accountNumber
      }
    ).subscribe(result => this.displayVoucher(result))
  }

  private displayVoucher(image: Blob) {
    if (image && image.size> 0) {
      let reader = new FileReader();
      reader.addEventListener('load', () => {
        this.voucherImage = reader.result;
      }, false);
      reader.readAsDataURL(image);
    }
  }

  private isMobile() {
      //mobile
      if (window.screen.width < 768) { // 768px portrait
        this.mobile = true;
      }
   }

  downloadVoucher() {
    this.medicalAssistanceService.getVoucherImage(
      MemberAssistanceApi.GET_MEDICAL_ASSISTANCE_VOUCHER_IMAGE, {
        accountNumber: this.accountNumber
      }
    ).subscribe(result => {
      const a = document.createElement('a');
      a.href = URL.createObjectURL(result);
      a.download = 'voucher.png';
      document.body.appendChild(a);
      a.click();
    });
  }
}
