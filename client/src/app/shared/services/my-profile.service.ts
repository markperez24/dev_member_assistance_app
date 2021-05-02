import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { CommonService } from "./common.service";
import { MemberAssistanceApi } from "../constants/member.assistance.api";
import { Observable } from "rxjs";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { PhotoUploadDialogComponent } from "../../home/my-profile/photo-upload-dialog/photo-upload-dialog.component";
import { catchError, map, take } from "rxjs/operators";
import { AbstractService } from "./abstract.service";
import { ChangePasswordComponent } from "../../home/my-profile/change-password/change-password.component";
import { IdPhotoUploadComponent } from "../../home/my-profile/id-photo-upload/id-photo-upload.component";

@Injectable({providedIn: 'root'})
export class MyProfileService extends AbstractService{
  dialogRef: MatDialogRef<any> | undefined;
  constructor(private http: HttpClient,
              private commonService: CommonService,
              private dialog: MatDialog) {
    super();
  }

  getMyProfile(username: string): Observable<any> {
    return this.commonService.get(MemberAssistanceApi.GET_MY_PROFILE, {
      username: username
    });
  }

  getProfilePhoto(url: string, options: any): Observable<Blob> {
    return this.commonService.getNoParams(url, options);
  }

  updateProfile(profile: any) {
    return this.commonService.post(MemberAssistanceApi.POST_UPDATE_MY_PROFILE, profile);
  }

  changePassword(urlStr: string, value: any): Observable<any> {
    return this.commonService.post(
      urlStr, value
    );
  }

  openPhotoUploadDialog(options: any) {
    this.dialogRef = this.dialog.open(PhotoUploadDialogComponent, {
      data: {
        title: options.title,
      }
    });
  }

  closePhotoUploadDialog(): Observable<any> {
    if(this.dialogRef) {
      return this.dialogRef.afterClosed()
        .pipe(take(1),
          map(res => {
              return res;
            }
          ));
    } else {
      return new Observable<any>();
    }
  }

  openIdUploadDialog(options: any) {
    this.dialogRef = this.dialog.open(IdPhotoUploadComponent, {
      data: {
        title: options.title,
      }
    });
  }

  closeIdUploadDialog(): Observable<any> {
    if(this.dialogRef) {
      return this.dialogRef.afterClosed()
        .pipe(take(1),
          map(res => {
              return res;
            }
          ));
    } else {
      return new Observable<any>();
    }
  }


  openChangePasswordDialog() {
    this.dialogRef = this.dialog.open(ChangePasswordComponent, {
      width: "400px",
    });
  }

  closeChangePasswordDialog(): Observable<any> {
    if(this.dialogRef) {
      return this.dialogRef.afterClosed()
        .pipe(take(1),
          map(res => {
              return res;
            }
          ));
    } else {
      return new Observable<any>();
    }
  }
}
