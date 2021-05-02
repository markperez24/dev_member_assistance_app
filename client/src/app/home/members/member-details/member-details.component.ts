import { Component, OnInit, ViewChild } from '@angular/core';
import { MemberService } from '../../../shared/services/member.service';
import { MedicalAssistanceService } from '../../../shared/services/medical-assistance.service';
import { MemberAssistanceApi } from '../../../shared/constants/member.assistance.api';
import { ActivatedRoute } from '@angular/router';
import { MatTableDataSource } from "@angular/material/table";
import { MatPaginator } from "@angular/material/paginator";
import { NgxSpinnerService } from "ngx-spinner";
import { processing } from "../../../operators";


@Component({
  selector: 'app-member-details',
  templateUrl: './member-details.component.html',
  styleUrls: ['./member-details.component.scss']
})
export class MemberDetailsComponent implements OnInit {
  displayedColumns: string [] = ['patientName', 'memberId', 'voucherNumber', 'dateAwarded', 'amount'];
  dataSource = new MatTableDataSource<any>([]);
  // get reference to paginator
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  short = 'short';

  isLoading: boolean;
  queryParam: any;
  accountNumber: any;
  fullName: any;
  fullAddress: any;
  dateOfBirth: any;
  gender: any;
  contactNumber: any;

  voucherImage: any;
  profilePhoto: string | ArrayBuffer | null = null;
  //timeStamp = (new Date()).getTime();

  constructor(private memberService: MemberService,
              private spinnerService: NgxSpinnerService,
              private route: ActivatedRoute) {
    this.isLoading = true;
  }

  ngOnInit(): void {
    this.isLoading = true;
    this.route.queryParams.subscribe(params => {
      this.queryParam = params['member'];
    });
    //console.log("Query Param: ", this.queryParam);
    this.paginator.pageSizeOptions = [5, 10, 20];
    this.paginator.pageSize = 20;
    this.dataSource.paginator = this.paginator;
    this.getMemberDetails();
  }

  getMemberDetails() {
    this.memberService.displayMemberDetails(
      MemberAssistanceApi.GET_MEMBER_DETAILS, {
        member : this.queryParam
      }).pipe(processing(this.spinnerService))
      .subscribe(result => {
      this.displayMemberDetails(result);
      this.getProfileImage();
      this.getVoucherImage();
      this.isLoading = false;
    });
  }

  displayMemberDetails(result: any) {
    this.accountNumber = result.memberDetails.accountNumber;
    this.fullName = result.memberDetails.fullName;
    this.fullAddress = result.memberDetails.fullAddress;
    this.dateOfBirth = result.memberDetails.dateOfBirth;
    this.contactNumber = result.memberDetails.contactNumber;
    this.gender = result.memberDetails.gender;

    this.displayMemberMedicalAssistance(result);
  }

  displayMemberMedicalAssistance(result : any) {
    let medicalAssistanceList = result.memberDetails.medicalAssistanceList;
    medicalAssistanceList.forEach(function (i: any) {
      i.patientFullName = i.firstName.concat(' ').concat(i.middleName)
        .concat(' ').concat(i.lastName);
    });
    this.dataSource = new MatTableDataSource<any>(medicalAssistanceList);
    this.dataSource.paginator = this.paginator;
  }

  getProfileImage(): any {
      this.memberService.getImage(
        MemberAssistanceApi.GET_PROFILE_PHOTO_BY_ACCOUNT_NUMBER, {
          accountNumber: this.accountNumber
        }
      ).subscribe(result => this.displayProfile(result));
  }

  private displayProfile(image: Blob) {
    if (image && image.size > 0) {
      let reader = new FileReader();
      reader.addEventListener('load', () => {
        this.profilePhoto = reader.result;
      }, false);
      reader.readAsDataURL(image);
    }
  }

  private getVoucherImage() {
    this.memberService.getImage(
      MemberAssistanceApi.GET_MEMBER_VOUCHER_IMAGE, {
        accountNumber: this.accountNumber
      }
    ).subscribe(result => this.displayVoucher(result))
  }

  private displayVoucher(image: Blob) {
    if (image && image.size > 0) {
      let reader = new FileReader();
      reader.addEventListener('load', () => {
        this.voucherImage = reader.result;
      }, false);
      reader.readAsDataURL(image);
    }
  }
}
