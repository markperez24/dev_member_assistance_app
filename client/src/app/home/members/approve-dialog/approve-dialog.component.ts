import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MyErrorStateMatcher } from '../../../public/registration/registration.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AdminService } from '../../../shared/services/admin.service';
import { MemberAssistanceApi } from '../../../shared/constants/member.assistance.api';
import { ApplicationApproveModel } from "../../../shared/model/application.model";

@Component({
  selector: 'app-approve-dialog',
  templateUrl: './approve-dialog.component.html',
  styleUrls: ['./approve-dialog.component.scss']
})
export class ApproveDialogComponent implements OnInit {
  form!: FormGroup;
  matcher = new MyErrorStateMatcher();
  application!: ApplicationApproveModel;
  constructor(public dialogRef: MatDialogRef<ApproveDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private adminService: AdminService) {
  }

  ngOnInit(): void {
    this.initFormFields();
    this.getApplication();
  }

  close() {
    this.dialogRef.close();
  }

  onSubmit() {
    if(this.form.invalid) {
      return;
    }
    let approveApplication  = this.form.value;
    approveApplication.medicalAssistanceId = this.application.medicalAssistanceId;
    approveApplication.fullName = this.application.fullName;
    this.adminService.approveApplication(
      MemberAssistanceApi.POST_APPROVE_APPLICATION,
      approveApplication
    ).subscribe(res=> {
      if(res && res.success) {
        this.dialogRef.close({success: true});
      } else {
        this.dialogRef.close({error : res.error});
      }
    });
  }

  private getApplication() {
      this.adminService.getApplication(
        MemberAssistanceApi.GET_APPLICATION,
        { accountNumber: this.data.member.accountNumber }
      ).subscribe(res => {
        if(res && res.success) {
          this.application = res.application;
          this.populateFormFields(res.application);
        } else {
          console.log('Unable to get medical application.');
          this.dialogRef.close({error : res.error});
        }
      });
  }

  private initFormFields() {
    this.form = this.formBuilder.group({
      fullName: ['', Validators.required],
      hospitalName: ['', Validators.required],
      amount: ['', [Validators.required, Validators.max(3000)]],
      accountNumber: ['', Validators.required],
      voucherNumber: ['', Validators.required]
    });
  }

  private populateFormFields(application: any) {
    this.form = this.formBuilder.group({
      fullName: [{value: application.fullName, disabled: true}, Validators.required],
      hospitalName: [application.hospitalName, Validators.required],
      amount: ['', [Validators.required, Validators.max(3000)]],
      accountNumber: [application.accountNumber, Validators.required],
      voucherNumber: ['', Validators.required]
    });
  }
}
