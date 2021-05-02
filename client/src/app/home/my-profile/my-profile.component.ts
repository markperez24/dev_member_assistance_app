import { Component, Input, OnInit } from '@angular/core';
import { MyProfileService } from '../../shared/services/my-profile.service';
import { DialogService } from '../../shared/services/dialog.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MyErrorStateMatcher } from '../../public/registration/registration.component';
import { KeycloakService } from 'keycloak-angular';
import { Router } from '@angular/router';
import { Profile } from '../../shared/model/profile-model.component';
import { MemberAssistanceMessages } from '../../shared/constants/member.assistance.messages';
import { MemberAssistanceApi } from '../../shared/constants/member.assistance.api';
import { Subject } from 'rxjs';
import { NgxSpinnerService } from 'ngx-spinner';
import { processing } from '../../operators';
import { UtilService } from "../../shared/services/util.service";
import { debounceTime, distinctUntilChanged } from "rxjs/operators";
import { isEmpty, isNull } from 'lodash';

export interface MyProfileActions {
  photoActionStr: string;
  idActionStr: string;
}

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.scss']
})
export class MyProfileComponent implements OnInit {
  @Input() isLoading = new Subject();

  form!: FormGroup;
  matcher = new MyErrorStateMatcher();
  onEdit: boolean;
  processing: boolean;
  isRateLimitReached: boolean;
  //readonly: boolean = trude;
  profile: Profile;

  profilePhoto: string | ArrayBuffer | null = null;
  hasProfilePhoto: boolean;
  hasId: boolean;
  timeStamp = (new Date()).getTime();

  actions: MyProfileActions = {
    photoActionStr: 'Upload Photo',
    idActionStr: 'Upload ID'
  };

  inputClassArr : string[];
  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private dialogService: DialogService,
    private keycloakService: KeycloakService,
    private myProfileService: MyProfileService,
    private spinnerService: NgxSpinnerService,
    private utilService: UtilService
  ) {
    this.onEdit = false;
    this.processing = false;
    this.isRateLimitReached = false;
    this.profile = {} as Profile;
    this.hasProfilePhoto = false;
    this.hasId = false;
    this.inputClassArr = [];
  }

  ngOnInit(): void {
    this.initValidationForm();
    if (this.keycloakService.isLoggedIn()) {
      this.getMemberProfile();
      this.showProfilePhoto();
    } else {
      this.router.navigate(['/']);
    }
  }

  getMemberProfile() {
    //this.spinnerService.show();
    this.myProfileService.getMyProfile(
      this.keycloakService.getUsername()
    )
      .pipe(
        processing(this.spinnerService)
      )
      .subscribe(res => {
        if (res && res.profile) {
          this.profile = res.profile;
          this.populateFormFields(res.profile);
        } else if(res.error){
          this.dialogService.open(
            {
              title: 'Warning',
              message: 'Unable to display your profile.'
            }
          )
        }
      })
  };

  initValidationForm() {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', Validators.required],
      middleName: ['', Validators.required],
      lastName: ['', Validators.required],
      age: ['', [Validators.required, Validators.min(8), Validators.max(120)]],
      dateOfBirth: ['', Validators.required],
      gender: ['', Validators.required],
      contactNumber: ['', Validators.required],
      address1: ['', Validators.required],
      address2: ['', Validators.required],
      city: ['', Validators.required],
      province: ['', Validators.required]
    });
  }

  populateFormFields(profile: any) {
    this.form = this.formBuilder.group({
      email: [{value: profile.emailAddress, disabled: true}, [Validators.required, Validators.email]],
      firstName: [{value: profile.firstName, disabled: true}, Validators.required],
      middleName: [{value: profile.middleName, disabled: true}, Validators.required],
      lastName: [{value: profile.lastName, disabled: true}, Validators.required],
      gender: [{value: profile.gender, disabled: true}, Validators.required],
      dateOfBirth: [{value: profile.dateOfBirth, disabled: true}, Validators.required],
      age: [{
        value: profile.age,
        disabled: !this.onEdit
      }, [Validators.required, Validators.min(12), Validators.max(120)]],
      contactNumber: [{value: profile.contactNumber, disabled: !this.onEdit}, Validators.required],
      address1: [{value: profile.address2, disabled: !this.onEdit}, Validators.required],
      address2: [{value: profile.address1, disabled: !this.onEdit}, Validators.required],
      province: [{value: profile.province, disabled: !this.onEdit}, Validators.required],
      city: [{value: profile.city, disabled: !this.onEdit}, Validators.required],
    });
  }

  edit() {
    this.onEdit = true;
    this.enableFields();
    this.form.controls.contactNumber.valueChanges
      .pipe(
        debounceTime(500),
        distinctUntilChanged()
      ).subscribe(number => this.form.controls.contactNumber.setValue(this.utilService.formatMobile(number)));
  }

  onSubmit() {
    this.processing = true;
    if (this.form.invalid) {
      return;
    }
    this.myProfileService.updateProfile(
      this.form.value
    ).subscribe(res => {
      if (res && res.status == MemberAssistanceMessages.RES_STATUS_SUCCESS) {
        this.populateFormFields(res.profile);
        this.dialogService.open(
          {
            title: MemberAssistanceMessages.TITLE_PROFILE_UPDATE_SUCCESS,
            message: res.message
          }
        )
      } else if (res && res.status == MemberAssistanceMessages.RES_STATUS_ERROR) {
        this.populateFormFields(this.profile);
        this.dialogService.open(
          {
            title: MemberAssistanceMessages.TITLE_PROFILE_UPDATE_ERROR,
            message: res.message
          }
        )
      }
      this.disableFields();
      this.onEdit = false;
      this.processing = false;
    }, error => {
      this.onEdit = false;
      this.processing = false;
      this.isRateLimitReached = true;
      //console.log('error');
    })
  }

  cancel() {
    this.populateFormFields(this.profile);
    this.disableFields();
    this.onEdit = false;
  }

  private enableFields() {
    this.form.controls.age.enable();
    this.form.controls.province.enable();
    this.form.controls.contactNumber.enable();
    this.form.controls.address1.enable();
    this.form.controls.address2.enable();
    this.form.controls.city.enable();
  }

  private disableFields() {
    this.form.disable();
  }

  uploadProfilePhoto() {
    this.myProfileService.openPhotoUploadDialog({title: MemberAssistanceMessages.TITLE_UPLOAD_PROFILE_PHOTO});
    this.myProfileService.closePhotoUploadDialog()
      .subscribe(res => {
        if (res && res.newUpload) {
          //this reloads the profile photo;
          //this.timeStamp = (new Date()).getTime();
          this.showProfilePhoto();
          this.dialogService.open(
            {
              title: MemberAssistanceMessages.TITLE_PROFILE_PHOTO_UPLOAD_SUCCESS,
              message: res.message
            }
          )
        }
      });
  }

  uploadIdPhoto() {
    this.myProfileService.openIdUploadDialog({title: MemberAssistanceMessages.TITLE_UPLOAD_ID_PHOTO});
    this.myProfileService.closeIdUploadDialog().subscribe(res => {
      if (res && res.newUpload) {
        //this reloads the profile photo;
        //this.timeStamp = (new Date()).getTime();
        //this.showProfilePhoto();
        this.dialogService.open(
          {
            title: MemberAssistanceMessages.TITLE_ID_PHOTO_UPLOAD_SUCCESS,
            message: res.message
          }
        )
      }
    });
  }

  changePassword() {
    this.myProfileService.openChangePasswordDialog();
    this.myProfileService.closeChangePasswordDialog()
      .subscribe(res => {
        if (res && res.status == MemberAssistanceMessages.RES_STATUS_SUCCESS) {
          this.dialogService.open(
            {
              title: MemberAssistanceMessages.TITLE_CHANGE_PASSWORD_SUCCESS,
              message: res.message
            }
          )
        } else if (res && res.status == MemberAssistanceMessages.RES_STATUS_ERROR) {
          this.dialogService.open(
            {
              title: MemberAssistanceMessages.TITLE_CHANGE_PASSWORD_ERROR,
              message: res.message
            }
          )
        }
      });
  }

  showProfilePhoto(): any {
    if (this.timeStamp) {
      let url = MemberAssistanceApi.GET_PROFILE_PHOTO;
      this.myProfileService.getProfilePhoto(
        url, {responseType: 'blob' as 'json'}
      ).subscribe(image => this.loadImage(image),
        err => console.log('Cannot load profile photo.'));
    }
  }

  private loadImage(image: Blob) {
    if (image && image.size > 0) {
      this.hasProfilePhoto = true;
      let reader = new FileReader();
      reader.addEventListener('load', () => {
        this.profilePhoto = reader.result;
      }, false);
      reader.readAsDataURL(image);
      this.updateProfilePhotoAction();
    }
  }

  private updateProfilePhotoAction() {
    this.actions.photoActionStr = MemberAssistanceMessages.BTN_LABEL_UPDATE_PROFILE_PHOTO;
  }

  shrinkText(txt: string) {
    if (txt.length > 75) {
      return 8;
    } else if (txt.length > 50) {
      return 10;
    } else if (txt.length > 30) {
      return 12;
    } else {
      return 18;
    }
  }

  updateStyling(newClass: string) {
    if(this.inputClassArr.indexOf(newClass) == -1) {
      if(!isEmpty(newClass) || !isNull(newClass)) {
        this.inputClassArr.push(newClass);
      }
    }
    return this.inputClassArr;
  }

  initStyling(initialClass: string) {
    this.inputClassArr = [];

    if(!isEmpty(initialClass) || !isNull(initialClass)) {
      this.inputClassArr.push(initialClass);
    }
    return this.inputClassArr;
  }
}
