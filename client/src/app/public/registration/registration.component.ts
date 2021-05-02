import { Component, OnInit } from '@angular/core';
import { RegistrationService } from '../../shared/services/registration.service';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  FormGroupDirective,
  NgForm,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { UserService } from '../../shared/services/user.service';
import { MemberAssistanceApi } from '../../shared/constants/member.assistance.api';
import { ErrorStateMatcher } from '@angular/material/core';
import { DialogService } from '../../shared/services/dialog.service';
import { processing } from '../../operators';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { NgxSpinnerService } from 'ngx-spinner';
import { MemberAssistanceConstants } from '../../shared/constants/member.assistance.constants';
import { CustomValidatorsService } from '../../shared/services/custom-validators.service';
import { UtilService } from '../../shared/services/util.service';

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {
  displayName: string;
  siteKey: string;
  isFormDisplay: boolean;
  exists: boolean;
  registerForm!: FormGroup;
  processing$: Subject<boolean>;
  submitted = false;

  error: string;
  matcher = new MyErrorStateMatcher();
  today = new Date();
  hideConfirmPassword = true;
  hidePassword = true;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private dialogService: DialogService,
              private registrationService: RegistrationService,
              private keyCloakService: KeycloakService,
              private userService: UserService,
              private customValidatorsService: CustomValidatorsService,
              private spinnerService: NgxSpinnerService,
              private utilService: UtilService) {
    this.displayName = 'Member Assistance Registration';
    this.siteKey = MemberAssistanceConstants.SITE_KEY;
    this.processing$ = new Subject<boolean>();
    this.isFormDisplay = false;
    this.error = '';
    this.exists = true;
    this.today.setFullYear(this.today.getFullYear() - 12);
    // redirect to home if already logged in
    this.today.setDate(this.today.getDate());
    if (localStorage.getItem('isLoggedIn') != null) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit(): void {
    //TODO: uncomment
    //document.addEventListener('contextmenu', event => event.preventDefault());
    //this.isFormDisplay = true;
    this.registerForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      middleName: ['', Validators.required],
      lastName: ['', Validators.required],
      gender: ['', Validators.required],
      contactNumber: ['', [Validators.required, Validators.minLength(13)]],
      age: ['', [Validators.required, Validators.min(12), Validators.max(120)]],
      dateOfBirth: ['', Validators.required],
      address1: ['', Validators.required],
      address2: ['', Validators.required],
      city: ['', Validators.required],
      province: ['', Validators.required],
      emailAddress: [null, [Validators.required, Validators.email]],
      password: ['', Validators.compose(
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
      recaptchaReactive: [null, Validators.required],
      captchaResult: [null, Validators.required],
      confirmPassword: [null, Validators.compose([Validators.required])]
    }, {
      // check whether our password and confirm password match
      validator: this.customValidatorsService
        .passwordMatchValidator('password', 'confirmPassword')
    });
    this.registerForm.controls.emailAddress.valueChanges
      .pipe(
        debounceTime(MemberAssistanceConstants.SEARCH_DELAY),
        distinctUntilChanged()
      ).subscribe(email => {
      this.verifyEmail(email);
    });
    this.registerForm.controls.contactNumber.valueChanges
      .pipe(
        debounceTime(500),
        distinctUntilChanged()
      ).subscribe(number => this.registerForm.controls.contactNumber.setValue(this.utilService.formatMobile(number)));
    /*this.registerForm.controls.recaptchaReactive.valueChanges
      .pipe(
        debounceTime(MemberAssistanceConstants.SEARCH_DELAY),
        distinctUntilChanged()
      ).subscribe(response => {
      this.verifyRecaptcha(response);
    });*/

  }

  loadRegistrationForm() {
    this.isFormDisplay = true;
  }

  onVideoEnded($event: boolean) {
    this.loadRegistrationForm();
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit() {
    // stop here if form is invalid
    if (!this.registerForm.valid) {
      return;
    }
    this.userService.register(
      MemberAssistanceApi.POST_REGISTER_USER,
      this.registerForm.value)
      .pipe(
        processing(this.spinnerService)
      )
      .subscribe(
        data => {
          if (data && data.isRegistered) {
            this.dialogService.open(
              {message: 'Registration is complete.'}
            );
            this.dialogService.close().subscribe(res => {
              console.log('The dialog was closed');
              this.router.navigate(['/'], {queryParams: {registered: true}});
            });
          }
        });
  }

  cancel() {
    this.router.navigate(['/']);
  }

  private verifyEmail(email: string) {
    this.exists = false;
    if (this.registerForm.controls.emailAddress.invalid) {
      return;
    }

    this.userService.verifyIfExists(
      MemberAssistanceApi.GET_EMAIL_EXISTS, email
    ).pipe(
      processing(this.spinnerService)
    ).subscribe(
      data => {
        if (data) {
          this.exists = data.exists;
        }
        if (data && data.exists) {
          this.registerForm.controls.emailAddress.setErrors({emailExists: this.exists});
        }
      });
  }

  resolved(captchaResponse: string) {
    this.verifyRecaptcha(captchaResponse)
  }

  verifyRecaptcha(captchaResponse: string) {
    this.registrationService.verifyCaptcha(
      MemberAssistanceApi.POST_VERIFY_CAPTCHA,
      {reCaptcha: captchaResponse}
    ).subscribe(data => {
      if (data && data.success) {
        this.registerForm.controls.captchaResult.setValue(data.success);
      } else this.registerForm.controls.captchaResult.setErrors({invalidResponse: true})
    });
  }

  errorMessage(ctrl: AbstractControl, error: string): string {
    return error == 'required' ? ' is required.'
      : error == 'maxlength' ? ' maximum length is ' + ctrl.getError(error).requiredLength
        : error == 'minlength' ? `${' minimum length is ' + ctrl.getError(error).requiredLength}`
          : error == 'hasNumber' ? ' must have a number.'
            : error == 'hasCapitalCase' ? ' must have a capital case letter.'
              : error == 'hasSmallCase' ? ' must have a small case letter.' : '';
    //: error == 'hasSpecialCharacter' ? ' must have a special character': '';
  }

  errors(ctrl: AbstractControl): string[] {
    return ctrl.errors ? Object.keys(ctrl.errors) : [];
  }
}
