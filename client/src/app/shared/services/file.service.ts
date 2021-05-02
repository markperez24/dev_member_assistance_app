import { Injectable } from '@angular/core';
import { AbstractService } from './abstract.service';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommonService } from './common.service';
import { FileInfo } from '../../home/my-profile/id-photo-upload/id-photo-upload.component';
import { fileConfiguration } from '../../../environments/environment';
import { NgxImageCompressService } from "ngx-image-compress";
import { forEach } from "lodash";
import { MemberAssistanceMessages } from "../constants/member.assistance.messages";

@Injectable({providedIn: 'root'})
export class FileService extends AbstractService {
  constructor(private http: HttpClient,
              private commonService: CommonService,
              private imageCompress: NgxImageCompressService) {
    super();
  }

  uploadFile(url: string, file: File): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const req = new HttpRequest('POST',
      `${this.commonService.getApiUrl()}`.concat(url),
      formData, {
        reportProgress: true,
        responseType: 'json'
      });
    return this.http.request(req);
  }

  uploadFiles(url: string, files: FileInfo[]): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    forEach(files, i => {
      formData.append(i.name, i.file);
    });
    const req = new HttpRequest('POST',
      `${this.commonService.getApiUrl().concat(url)}`,
      formData, {
        reportProgress: true,
        responseType: 'json'
      });
    return this.http.request(req);
  }

  checkFileSize(file: File) {
    let message = null;
    // The size of the file.
    const fileSize = Math.round((file.size / fileConfiguration.photo_max_size));
    if (fileSize >= fileConfiguration.photo_max_size) {
      message = MemberAssistanceMessages.ERROR_MSG_FILE_MAX_SIZE;
    }
    return message;
  }

  previewPhoto(file: File): Observable<any> {
    let reader = new FileReader();
    reader.readAsDataURL(file);
    return new Observable((observer) => {
      reader.onload = (_event) => {
        let img = reader.result;
        if (typeof img == 'string') {
          this.imageCompress.compressFile(img, 1, 50, 50).then(
            result => {
              observer.next(result);
              observer.complete();
            }
          );
        }
      };
    });
  }

  getIdPhoto(url: string, params: any, options: any): Observable<Blob> {
    return this.commonService.getNoParams(url.concat(encodeURIComponent(params.side)), options);
  }

  checkFileExtension(filename: string, type: string) {
    let allowedExtensions: string[] = [];
    if (type == 'image') {
      allowedExtensions = allowedExtensions.concat(['.jpg', '.jpeg', '.png']);
    }
    let matchExtension = false;
    forEach(allowedExtensions, i => {
      if (filename.toLowerCase().endsWith(i)) {
        matchExtension = true;
      }
    });
    return matchExtension;
  }

  strToFile(dataURI: any, type: string, filename: string): File {
    let byteString = atob(dataURI.split(',')[1]);
    let ab = new ArrayBuffer(byteString.length);
    let ia = new Uint8Array(ab);

    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }

    let blob = new Blob([ab], {type: 'image/jpeg'});
    /*let blob = null;
    if (type == 'jpg' || type == 'jpeg') {
      blob = new Blob([ab], {type: 'image/jpeg'});
    } else {
      blob = new Blob([ab], {type: 'image/png'});
    }*/
    return new File([blob], filename);
  }
}
