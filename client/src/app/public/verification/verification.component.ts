import { Component, OnInit } from '@angular/core';
import { CommonService } from '../../shared/services/common.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { VerificationService } from '../../shared/services/verification.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MemberAssistanceApi } from "../../shared/constants/member.assistance.api";
import { isNull } from 'lodash';

@Component({
  selector: 'app-verification',
  templateUrl: './verification.component.html',
  styleUrls: ['./verification.component.scss']
})
export class VerificationComponent implements OnInit {
  displayName: string;


  constructor(private route: ActivatedRoute,
              private router: Router,
              private verificationService: VerificationService,
              private spinnerService: NgxSpinnerService) {
    this.displayName = 'OTP Verification'
  }

  ngOnInit(): void {
    let id = this.route.snapshot.paramMap.get('id');
    if(!isNull(id)) {
      this.verificationService.init(MemberAssistanceApi.GET_INIT_VERIFICATION, id)
        .subscribe(res => {

        });
    } else {
      this.router.navigate(["/"]);
    }
  }
}
