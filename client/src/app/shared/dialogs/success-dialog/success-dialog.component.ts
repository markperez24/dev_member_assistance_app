import { Component, Inject, OnInit } from '@angular/core';
import { CommonService } from "../../services/common.service";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'app-dialog',
  templateUrl: './success-dialog.component.html',
  styleUrls: ['./success-dialog.component.scss']
})
export class SuccessDialogComponent implements OnInit {
  title: string;
  message: string;
  constructor(
    public dialogRef: MatDialogRef<SuccessDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private commonService: CommonService) {
    this.title = 'Success';
    this.message = data.message;
  }
  ngOnInit(): void {
    if(this.data.title) {
      this.title = this.data.title;
    }
  }
}
