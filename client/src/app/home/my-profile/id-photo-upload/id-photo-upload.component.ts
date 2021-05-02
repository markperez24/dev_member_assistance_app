import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FileService } from '../../../shared/services/file.service';
import { MemberAssistanceApi } from '../../../shared/constants/member.assistance.api';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { isNull, isEqual, isFinite, isEmpty } from 'lodash';
import { MemberAssistanceMessages } from "../../../shared/constants/member.assistance.messages";
import { fileConfiguration } from "src/environments/environment";
import { NgxImageCompressService } from "ngx-image-compress";

export interface FileInfo {
  name: string;
  file: File;
}

@Component({
  selector: 'app-id-photo-upload',
  templateUrl: './id-photo-upload.component.html',
  styleUrls: ['./id-photo-upload.component.scss']
})
export class IdPhotoUploadComponent implements OnInit {
  title: string;
  url: string;
  hasIdPhoto: boolean;
  message: string | null;
  frontMessage: string | null;
  backMessage: string | null;
  uploading: boolean;

  @ViewChild('fileInput', {static: false}) fileInput: any;
  frontFile!: FileInfo | null;
  frontFilePreview!: string;
  backFile!: FileInfo | null;
  backFilePreview!: string;

  progress = 0;
  fileInfo: Observable<any>;

  constructor(public dialogRef: MatDialogRef<IdPhotoUploadComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private fileService: FileService,
              private imageCompress: NgxImageCompressService) {
    this.title = data.title;
    this.hasIdPhoto = false;
    this.message = null;
    this.frontMessage = null;
    this.backMessage = null;
    this.url = data.url;
    this.uploading = false;
    this.fileInfo = new Observable<any>();
  }

  ngOnInit(): void {
    this.getIdPhotos();
  }

  getIdPhotos() {
    let url = MemberAssistanceApi.GET_ID_PHOTO;
    let front = 'front', back = 'back';
    this.fileService.getIdPhoto(
      url, {side: front}, {responseType: 'blob' as 'json'}
    ).subscribe(image =>
      this.loadImages(image, front)
    );
    this.fileService.getIdPhoto(
      url, {side: back}, {responseType: 'blob' as 'json'}
    ).subscribe(image =>
      this.loadImages(image, back)
    );
  }

  onFileAdded($event: any, side: string) {
    const file = $event.target.files[0];
    this.progress = 0;

    let f: FileInfo = {} as FileInfo;
    f.name = side;
    f.file = file;
    const fileSize = Math.round((f.file.size / fileConfiguration.photo_compression_enable));
    if (side == 'front') {
      let isFileValid = false;
      this, this.frontFile = null;
      if (this.fileService.checkFileExtension(f.file.name, 'image')) {
        isFileValid = true;
      }
      if (!isFileValid) {
        this.frontMessage = MemberAssistanceMessages.ERROR_MSG_FILE_TYPE_NOT_ALLOWED;
        return;
      }
      this.frontMessage = this.fileService.checkFileSize(f.file);
      if (fileSize >= fileConfiguration.photo_compression_enable) {
        //compress
        this.fileService.previewPhoto(f.file)
          .subscribe(img => {
            this.frontFilePreview = img;
            f.file = this.fileService.strToFile(img, 'jpg', f.file.name);
            this.frontFile = f;
          });
      } else {
        this.fileService.previewPhoto(f.file)
          .subscribe(img => {
            this.frontFilePreview = img;
            f.file = this.fileService.strToFile(img, 'jpg', f.file.name);
            this.frontFile = f;
          });
      }
    } else if (side == 'back') {
      let isFileValid = false;
      this.backFile = null;
      if (this.fileService.checkFileExtension(f.file.name, 'image')) {
        isFileValid = true;
      }
      if (!isFileValid) {
        this.backMessage = MemberAssistanceMessages.ERROR_MSG_FILE_TYPE_NOT_ALLOWED;
        return;
      }
      this.backMessage = this.fileService.checkFileSize(f.file);
      if (fileSize >= fileConfiguration.photo_compression_enable) {
        //compress
        this.fileService.previewPhoto(f.file)
          .subscribe(img => {
            this.backFilePreview = img;
            f.file = this.fileService.strToFile(img, 'jpg', f.file.name);
            this.backFile = f;
          });
      } else {
        this.backFile = f;
        this.fileService.previewPhoto(f.file)
          .subscribe(img => {
            this.backFilePreview = img;
          })
      }
    }
  }

  close() {
    this.dialogRef.close();
  }

  upload(): void {
    this.progress = 0;
    if (!isNull(this.frontFile) && !isNull(this.backFile)) {
      let currentFiles: (FileInfo)[] = new Array(this.frontFile, this.backFile);
      if (isFinite(currentFiles.length)) {
        this.fileService.uploadFiles(MemberAssistanceApi.POST_UPLOAD_ID_PHOTO, currentFiles)
          .subscribe(
            event => {
              if (event && event.type === HttpEventType.UploadProgress) {
                if (event.total && event.loaded) {
                  this.progress = Math.round(100 * event.loaded / event.total);
                }
              } else if (event instanceof HttpResponse) {
                this.message = event.body.message;
              }
            });
      }
    }
  }

  hasErrorMessage() {
    if (isNull(this.frontMessage) && isNull(this.backMessage) && isNull(this.message)) {
      return false;
    } else {
      return true;
    }
  }

  private loadImages(image: any, side: string) {
    if (isEqual(side, 'front') && image.size > 0) {
      this.hasIdPhoto = true;
      this.fileService.previewPhoto(image)
        .subscribe(img => {
          this.frontFilePreview = img;
        });
    }
    if (isEqual(side, 'back') && image.size > 0) {
      this.fileService.previewPhoto(image)
        .subscribe(img => {
          this.backFilePreview = img;
        });
    }
  }
}

