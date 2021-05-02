import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MyErrorStateMatcher } from '../../../public/registration/registration.component';
import { CustomValidatorsService } from '../../../shared/services/custom-validators.service';
import { MatDialogRef } from '@angular/material/dialog';
import { MemberAssistanceApi } from '../../../shared/constants/member.assistance.api';
import { CommonService } from '../../../shared/services/common.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {
  form!: FormGroup;
  matcher = new MyErrorStateMatcher();
  isProcessing: boolean;
  isProcessingLimitReached: boolean;
  hidePassword = true;
  hideConfirmPassword = true;

  constructor(
    public dialogRef: MatDialogRef<ChangePasswordComponent>,
    private formBuilder: FormBuilder,
    private customValidatorsService: CustomValidatorsService,
    private commonService: CommonService) {
    this.isProcessing = false;
    this.isProcessingLimitReached = false;
  }

  ngOnInit(): void {
    this.initValidationForm();
  }

  initValidationForm() {
    this.form = this.formBuilder.group({
      newPassword: ['', Validators.compose(
          [Validators.required,
          // must contain a number
          this.customValidatorsService.patternValidator(/\d/, {
            hasNumber: true
          }),
          // must contain a capital letter
          this.customValidatorsService.patternValidator(/[A-Z]/, {
            hasCapitalCase: true
          }),
          // must contain a lowercase letter
          this.customValidatorsService.patternValidator(/[a-z]/, {
            hasSmallCase: true
          }),
          // must contain a special character
          /*this.customValidatorsService.patternValidator(
            /[ !@#$%^&*()_+\-=\[\]{};':'\\|,.<>\/?]/, {
              hasSpecialCharacter: true
            }
          ),*/
          Validators.maxLength(16),
          Validators.minLength(8)
      ])],
    //)],
      confirmPassword: [null, Validators.compose([Validators.required])]
    }, {
      // check whether our password and confirm password match
      validator: this.customValidatorsService.passwordMatchValidator('newPassword', 'confirmPassword')
    });
  }

  changePassword() {
    this.isProcessing = true;
    this.commonService.post(
      MemberAssistanceApi.POST_MEMBER_CHANGE_PASSWORD,
      this.form.value
    ).subscribe(res => {
      this.isProcessing = false;
      this.isProcessingLimitReached = false;
      this.dialogRef.close(res);
    })
  }

  close() {
    this.dialogRef.close();
  }
}
