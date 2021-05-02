import { Component, OnInit, Inject } from '@angular/core';
import { HospitalService } from '../../../shared/services/hospital.service';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { debounceTime, distinctUntilChanged, first } from "rxjs/operators";
import { MemberAssistanceApi } from "../../../shared/constants/member.assistance.api";
import { ErrorStateMatcher } from "@angular/material/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Location } from '@angular/common';
import { UtilService } from "../../../shared/services/util.service";
import { NgxSpinnerService } from "ngx-spinner";
import { processing } from "../../../operators";

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-add-hospital-dialog',
  templateUrl: './add-hospital-dialog.component.html',
  styleUrls: ['./add-hospital-dialog.component.scss']
})
export class AddHospitalDialogComponent implements OnInit {
  isFormDisplay: boolean = false;

  registerForm!: FormGroup;
  loading = false;
  submitted = false;
  error!: string;

  matcher = new MyErrorStateMatcher();

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private hospitalService: HospitalService,
              private location: Location,
              public dialogRef: MatDialogRef<AddHospitalDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private spinnerService: NgxSpinnerService,
              private utilService: UtilService) {
  }

  ngOnInit(): void {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.isFormDisplay = true;

    this.registerForm = this.formBuilder.group({
      hospitalName: ['', Validators.required],
      address1: ['', Validators.required],
      address2: ['', Validators.required],
      location: ['', Validators.required],
      contactPerson: ['', Validators.required],
      designation: ['', Validators.required],
      contactNumber:['', Validators.required],
      emailAddress:[null, [Validators.required, Validators.email]],
      allotedBudget:['', [Validators.required, Validators.min(0)]],
    });

    this.registerForm.controls.contactNumber.valueChanges
      .pipe(
        debounceTime(500),
        distinctUntilChanged()
      ).subscribe(number => this.registerForm.controls.contactNumber.setValue(this.utilService.formatMobile(number)));
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit() {
    // this.submitted = true;

    // stop here if form is invalid
    if (!this.registerForm.valid) {
      return;
    }

    console.log("Registered Form: ", this.registerForm.value);

    this.loading = true;
    this.hospitalService.register(
      MemberAssistanceApi.POST_REGISTER_HOSPITAL,
      this.registerForm.value)
      .pipe(processing(this.spinnerService))
      .subscribe(
        data => {
          if (data && data.isRegistered) {
            this.dialogRef.close();
            this.router.navigate(['../hospitals'], {queryParams: {registered: true}});
          }
          this.loading = false;
        })
  }

  cancel() {
    this.dialogRef.close();
  }
}
