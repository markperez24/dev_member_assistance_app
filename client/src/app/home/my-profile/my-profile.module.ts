import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MyProfileRoutingModule } from './my-profile-routing.module';
import { MyProfileComponent } from './my-profile.component';
import { SharedModule } from '../../shared/shared.module';
import { AngularFileUploaderModule } from 'angular-file-uploader';
import { PhotoUploadDialogComponent } from './photo-upload-dialog/photo-upload-dialog.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { IdPhotoUploadComponent } from './id-photo-upload/id-photo-upload.component';


@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MyProfileRoutingModule,
    AngularFileUploaderModule,
    FlexLayoutModule.withConfig({addFlexToParent: false})
  ],
  declarations: [ MyProfileComponent,
                  PhotoUploadDialogComponent,
                  ChangePasswordComponent,
                  IdPhotoUploadComponent
  ]
})
export class MyProfileModule { }
