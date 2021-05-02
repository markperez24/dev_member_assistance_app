import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RegistrationRoutingModule } from './registration-routing.module';
import { RegistrationComponent } from './registration.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { SafePipe } from '../../safe.pipe';
import { YouTubePlayerModule } from '@angular/youtube-player';
import { RegistrationVideoComponent } from './registration-video/registration-video.component';
import { SharedModule } from '../../shared/shared.module';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { RecaptchaFormsModule, RecaptchaModule } from "ng-recaptcha";

@NgModule({
  imports: [
    CommonModule,
    RegistrationRoutingModule,
    SharedModule,
    YouTubePlayerModule,
    FlexLayoutModule.withConfig({addFlexToParent: false}),
    RecaptchaModule,
    RecaptchaFormsModule
  ],
  declarations: [
    RegistrationComponent,
    RegistrationVideoComponent,
    SafePipe
  ],
  exports : [
    MatFormFieldModule, MatInputModule,
    SafePipe
  ]
})
export class RegistrationModule { }
