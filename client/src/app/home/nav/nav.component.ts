import { Component, OnInit } from '@angular/core';
import { MemberAssistanceConstants } from "../../shared/constants/member.assistance.constants";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss']
})
export class NavComponent implements OnInit {

  companyName: any;
  constructor() { }

  ngOnInit(): void {
    this.companyName = MemberAssistanceConstants.COMPANY_NAME
  }

}
