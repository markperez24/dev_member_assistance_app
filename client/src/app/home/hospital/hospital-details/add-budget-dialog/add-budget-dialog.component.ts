import { Component, Inject, OnInit } from '@angular/core';
import { HospitalService } from '../../../../shared/services/hospital.service';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MemberAssistanceApi } from '../../../../shared/constants/member.assistance.api';
import { MemberAssistanceMessages } from '../../../../shared/constants/member.assistance.messages';
import { DialogService } from '../../../../shared/services/dialog.service';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog'; 
import { NgxSpinnerService } from 'ngx-spinner';
import { processing } from '../../../../operators';

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-add-budget-dialog',
  templateUrl: './add-budget-dialog.component.html',
  styleUrls: ['./add-budget-dialog.component.scss']
})
export class AddBudgetDialogComponent implements OnInit {
  hospitalId: any;
  hospitalName: any;
  
  isFormDisplay: boolean = false;

  form: FormGroup;
  isLoading = false;
  submitted = false;
  error: string;

  matcher = new MyErrorStateMatcher();

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private hospitalService: HospitalService,
              private spinnerService: NgxSpinnerService, 
              private dialogService: DialogService,
              public dialogRef: MatDialogRef<AddBudgetDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.hospitalId = data.hospitalId;
    this.hospitalName = data.hospitalName;
  }

  ngOnInit(): void {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.isFormDisplay = true;

    this.form = this.formBuilder.group({
      amount:['', [Validators.required, Validators.min(0)]],
      hospitalId:[this.hospitalId, []],
      hospitalName:[this.hospitalName, []],
    });
  }

  get f() {
    return this.form.controls;
  }

  onSubmit() {
    // stop here if form is invalid
    if (!this.form.valid) {
      this.showWarningMessage({
        title: MemberAssistanceMessages.TITLE_WARNING_COMPLETE_FIELDS,
        message: MemberAssistanceMessages.MESSAGE_WARNING_COMPLETE_FIELDS
      });
      return;
    }
    this.dialogService.openConfirmDialog(
      {
        title: MemberAssistanceMessages.TITLE_SAVE_HOSPITAL_ADDITIONAL_BUDGET,
        message: MemberAssistanceMessages.MSG_SAVE_HOSPITAL_ADDITIONAL_BUDGET
      }
    );
    console.log("Registered Form: ", this.form.value);
    this.dialogService.closeConfirm().subscribe(res => {
      if (res && res.continue) {
        this.isLoading = true;
        this.hospitalService.saveAdditionalBudget(
          MemberAssistanceApi.POST_SAVE_HOSPITAL_ADDITIONAL_BUDGET,
          this.form.value,
        ).pipe(processing(this.spinnerService))
        .subscribe(result => {
          console.log("Save Result: ", result);
          if (result && !result.error) {
            this.callSaveSuccessMessage();
          } else {
            console.log(result.error);
          }
        });
      }
    });

  }

  cancel() {
    this.dialogRef.close();
  }

  private showWarningMessage(options: any) {
    this.dialogService.open(
      options
    );
    this.dialogService.close().subscribe(res => {
    });
  }

  private callSaveSuccessMessage() {
    this.dialogService.open({
      title: MemberAssistanceMessages.TITLE_SUCCESS_ADD_BUDGET,
      message: MemberAssistanceMessages.MSG_SUCCESS_ADD_BUDGET
    });
    this.dialogService.close().subscribe(res => {
      this.dialogRef.close();
    });
  }
}
