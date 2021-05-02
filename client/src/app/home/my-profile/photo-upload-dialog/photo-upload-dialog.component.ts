import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FileService } from '../../../shared/services/file.service';
import { Observable } from 'rxjs';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { MemberAssistanceApi } from '../../../shared/constants/member.assistance.api';
import { isEqual, isNull } from 'lodash';
import { MemberAssistanceMessages } from "../../../shared/constants/member.assistance.messages";
import { fileConfiguration } from "../../../../environments/environment";
import { NgxImageCompressService } from "ngx-image-compress";

export interface FileInfo {
  url: string;
  name: string;
}

@Component({
  selector: 'app-photo-upload-dialog',
  templateUrl: './photo-upload-dialog.component.html',
  styleUrls: ['./photo-upload-dialog.component.scss']
})
export class PhotoUploadDialogComponent implements OnInit {
  title: string;
  message: string | null;
  uploading: boolean;
  newUpload: boolean;

  @ViewChild('fileInput', {static: false}) fileInput: any;

  currentFile: File | undefined;
  progress = 0;
  fileInfo: Observable<any>;

  constructor(
    public dialogRef: MatDialogRef<PhotoUploadDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fileService: FileService,
    private imageCompress: NgxImageCompressService) {
    this.title = data.title;
    this.message = '';
    this.uploading = false;
    this.newUpload = false;
    this.fileInfo = new Observable<any>();
  }

  ngOnInit(): void {
  }

  onFileAdded($event: any) {
    let file = $event.target.files[0];
    let isFileValid = false;

    if (this.fileService.checkFileExtension(file.name, 'image')) {
      isFileValid = true;
    }

    if (!isFileValid) {
      file = null;
      this.message = MemberAssistanceMessages.ERROR_MSG_FILE_TYPE_NOT_ALLOWED;
      return;
    }

    this.currentFile = file;
    this.message = this.fileService.checkFileSize(file);
  }

  // this.imageCompress.uploadFile().then(({image, orientation}) => {
  //       console.log('Image:',image);
  //       console.log('orientation:',orientation);
  //   this.imgResultBeforeCompress = image;
  //   console.warn('Size in bytes was:', this.imageCompress.byteCount(image));

  //   this.imageCompress.compressFile(image, orientation, 50, 50).then(
  //     result => {
  //       this.imgResultAfterCompress = result;
  //       console.warn('Size in bytes is now:', this.imageCompress.byteCount(result));
  //     }
  //   );
  // });

  close() {
    if (this.dialogRef)
      this.dialogRef.close();
  };

  closeOnSuccess() {
    this.dialogRef.close({
      newUpload: this.newUpload,
      message: this.message
    });
  }

  upload(): void {
    this.progress = 0;
    if (this.currentFile) {
      let filename = this.currentFile.name;
      //compress images greater than 1mb
      const fileSize = Math.round((this.currentFile.size / fileConfiguration.photo_compression_enable));
      if (fileSize >= fileConfiguration.photo_compression_enable) {
        let reader = new FileReader();
        reader.readAsDataURL(this.currentFile);
        reader.onload = (event: any) => {
          this.imageCompress.compressFile(event.target.result, 1, 50, 50)
            .then(result => {
              console.warn('Size after:', this.imageCompress.byteCount(result));
              this.fileService.uploadFile(MemberAssistanceApi.POST_UPLOAD_PROFILE_PHOTO, this.fileService.strToFile(result, 'jpg', filename))
                .subscribe(
                  event => {
                    if (event && event.type === HttpEventType.UploadProgress) {
                      if (event.total && event.loaded) {
                        this.progress = Math.round(100 * event.loaded / event.total);
                      }
                    } else if (event instanceof HttpResponse) {
                      let status = event.body.status;
                      if (status && isEqual(status, 'success')) {
                        this.newUpload = true;
                        this.message = event.body.message;
                        this.closeOnSuccess();
                      } else if (status && isEqual(status, 'error')) {
                        this.newUpload = false;
                        this.message = event.body.message;
                      }
                    }
                  },
                  err => {
                    this.progress = 0;
                    this.message = 'Could not upload the profile photo.';
                    this.currentFile = {} as File;
                  });
            });
        }
      } else {
        //no compression
        this.fileService.uploadFile(MemberAssistanceApi.POST_UPLOAD_PROFILE_PHOTO, this.currentFile)
          .subscribe(
            event => {
              if (event && event.type === HttpEventType.UploadProgress) {
                if (event.total && event.loaded) {
                  this.progress = Math.round(100 * event.loaded / event.total);
                }
              } else if (event instanceof HttpResponse) {
                let status = event.body.status;
                if (status && isEqual(status, 'success')) {
                  this.newUpload = true;
                  this.message = event.body.message;
                  this.closeOnSuccess();
                } else if (status && isEqual(status, 'error')) {
                  this.newUpload = false;
                  this.message = event.body.message;
                }
              }
            },
            err => {
              this.progress = 0;
              this.message = 'Could not upload the profile photo.';
              this.currentFile = {} as File;
            });
      }
    }
  }

  hasErrorMessage() {
    if (isNull(this.message)) {
      return false;
    } else {
      return true;
    }
  }
}
