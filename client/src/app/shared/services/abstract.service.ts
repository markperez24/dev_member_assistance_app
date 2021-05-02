import { Injectable } from '@angular/core';
import { MemberAssistanceConstants } from '../constants/member.assistance.constants';
import { of } from 'rxjs';

const API_URL = MemberAssistanceConstants.API_URL;

@Injectable({providedIn: 'root'})
export class AbstractService {

  constructor(){
  }

  getApiUrl(): string {
    return API_URL;
  }

  /**
   * Handle HTTP error
   */
  handleError(error: any) {
    const errMsg = (error.message) ? error.message :
      error.status ? `${error.status} - ${error.statusText}` : 'Server error';
    //console.error(errMsg); // log to console instead
    return of(errMsg);
  }
}
