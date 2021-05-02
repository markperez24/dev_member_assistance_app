import { Component, OnInit } from '@angular/core';
import { CommonService } from "../shared/services/common.service";
import { Subject } from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  processing: Subject<boolean>;

  constructor(private commonService: CommonService) {
    this.processing = new Subject<boolean>();
  }

  ngOnInit(): void {
    this.processing = this.commonService.isProcessing()
  }
}
