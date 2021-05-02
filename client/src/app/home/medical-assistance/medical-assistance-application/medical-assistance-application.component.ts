import { Component, OnInit } from '@angular/core';
import { MemberAssistanceApi } from '../../../shared/constants/member.assistance.api';
import { MedicalAssistanceService } from '../../../shared/services/medical-assistance.service';
import { MemberAssistanceMessages } from '../../../shared/constants/member.assistance.messages';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DialogService } from '../../../shared/services/dialog.service';
import { MyErrorStateMatcher } from '../../../public/registration/registration.component';
import { isEqual, isNull} from 'lodash';
import { debounceTime, distinctUntilChanged, map, startWith } from "rxjs/operators";
import { UtilService } from "../../../shared/services/util.service";
import { Observable } from "rxjs";

@Component({
  selector: 'app-medical-assistance-application',
  templateUrl: './medical-assistance-application.component.html',
  styleUrls: ['./medical-assistance-application.component.scss']
})
export class MedicalAssistanceApplicationComponent implements OnInit {
  isLoading: boolean;
  isRateLimitReached: boolean;

  messages: any;
  messageHeader: string;

  messageFooter: string;
  diagnoses: string[];
  assistanceTypes: string[];
  monthlyIncomeList: string[];
  hospitals: string[];
  filteredHospitals: Observable<string[]>;

  form!: FormGroup;
  matcher = new MyErrorStateMatcher();
  today = new Date();

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private dialogService: DialogService,
              private medicalAssistanceService: MedicalAssistanceService,
              private utilService: UtilService) {
    this.isLoading = true;
    this.isRateLimitReached = false;
    this.messageHeader = MemberAssistanceMessages.MSG_MAA_HEADER;
    this.messageFooter = MemberAssistanceMessages.MSG_MAA_FOOTER;
    this.diagnoses = [];
    this.assistanceTypes = [];
    this.monthlyIncomeList = [];
    this.hospitals = [];
    this.filteredHospitals =  new Observable<string[]>();
  }

  ngOnInit(): void {
    this.initFormFields();
    this.loadDetailsFromApi();
  }

  displayMedicalAssistanceForm(result: any) {
    let configurations = result.configurations;
    this.messages = configurations.maaMessages;
    this.diagnoses = this.diagnoses.concat(configurations.diagnosisList);
    this.monthlyIncomeList = this.monthlyIncomeList.concat(configurations.monthlyIncomeList);
    this.assistanceTypes = this.assistanceTypes.concat(configurations.assistanceTypeList);
    //this.hospitals = this.hospitals.concat(result.hospitals);
    this.hospitals = result.hospitals;
    this.filteredHospitals = this.form.controls.hospital.valueChanges
      .pipe(
        startWith(''),
        map(value => this._filter(value))
      );
    this.isLoading = false;
  }

  private initFormFields() {
    this.form = this.formBuilder.group({
      firstName: ['', Validators.required],
      middleName: ['', Validators.required],
      lastName: ['', Validators.required],
      gender: ['', Validators.required],
      contactNumber: ['', [Validators.required, Validators.minLength(13)]],
      age: ['', [Validators.required, Validators.min(0), Validators.max(120)]],
      dateOfBirth: ['', Validators.required],
      address1: ['', Validators.required],
      address2: ['', Validators.required],
      city: ['', Validators.required],
      province: ['', Validators.required],
      emailAddress: [null, [Validators.required, Validators.email]],
      diagnosis: [null, Validators.required],
      monthlyIncome: [null, Validators.required],
      assistanceType: [null, Validators.required],
      hospital: [null, Validators.required]
    });
    this.form.controls.contactNumber.valueChanges
      .pipe(
        debounceTime(500),
        distinctUntilChanged()
      ).subscribe(number => this.form.controls.contactNumber.setValue(this.utilService.formatMobile(number)));
  }

  private loadDetailsFromApi() {
    this.isLoading = true;
    this.medicalAssistanceService.getList(
      MemberAssistanceApi.GET_APPLY_MEDICAL_ASSISTANCE, {})
      .subscribe(result => {
        console.log(result);
        if (result && !result.error) {
          if(result.home) {
            this.router.navigate(['/home']);
          }
          this.displayMedicalAssistanceForm(result);
        } else {
          console.log(result.error);
        }
        this.isLoading = false;
        this.isRateLimitReached = false;
      }, error => {
        this.isRateLimitReached = true;
      })
  }

  onSubmit() {
    if (!this.form.valid) {
      this.showWarningMessage({
        title: MemberAssistanceMessages.TITLE_WARNING_COMPLETE_FIELDS,
        message: MemberAssistanceMessages.MESSAGE_WARNING_COMPLETE_FIELDS
      });
      return;
    }
    this.dialogService.openConfirmDialog(
      {
        title: MemberAssistanceMessages.TITLE_SAVE_MEDICAL_ASSISTANCE_APPLICATION,
        message: MemberAssistanceMessages.MSG_SAVE_MEDICAL_ASSISTANCE_APPLICATION
      }
    );
    this.dialogService.closeConfirm().subscribe(res => {
      if (res && res.continue) {
        this.isLoading = true;
        this.medicalAssistanceService.saveApplication(
          MemberAssistanceApi.POST_SAVE_MEDICAL_ASSISTANCE_APPLICATION,
          this.form.value
        ).subscribe(result => {
          console.log(result);
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
    this.dialogService.openCancelDialog(
      {
        title: MemberAssistanceMessages.TITLE_CANCEL_MEDICAL_ASSISTANCE_APPLICATION,
        message: MemberAssistanceMessages.MSG_CANCEL_MEDICAL_ASSISTANCE_APPLICATION
      }
    );
    this.dialogService.closeCancel().subscribe(res => {
      if (res && res.cancel) {
        this.router.navigate(['/medical-assistance']);
      }
    });
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
      title: MemberAssistanceMessages.TITLE_SUCCESS_APPLY_MEDICAL_ASSISTANCE,
      message: MemberAssistanceMessages.MSG_SUCCESS_APPLY_MEDICAL_ASSISTANCE
    });
    this.dialogService.close().subscribe(res => {
      this.router.navigate(['/medical-assistance']);
    });
  }

  isOthers() {
    let selectedDiagnosis = this.form.controls.diagnosis.value;
    if (!isNull(selectedDiagnosis)
      && isEqual(selectedDiagnosis.toLowerCase(), 'others')) {
        return true;
    }
    return false;
  }

  onChangeSelect($event: any) {
    let selectedDiagnosis = $event.value;
    let controlName = 'otherDiagnosis';
    if (!isNull(selectedDiagnosis)
      && isEqual(selectedDiagnosis.toLowerCase(), 'others')) {
      this.form.addControl(controlName, new FormControl('', Validators.required));
    } else {
      this.form.removeControl(controlName);
    }
  }

  private _filter(value: string): string[] {
    if(value == null || value == '') {
      return this.hospitals;
    }
    const filterValue = value.toLowerCase();
    return this.hospitals.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }
}
