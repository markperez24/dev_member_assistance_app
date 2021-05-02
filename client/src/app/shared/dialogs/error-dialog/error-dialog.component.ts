import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MemberAssistanceMessages } from "../../constants/member.assistance.messages";

@Component({
  selector: 'app-error-dialog',
  templateUrl: './error-dialog.component.html',
  styleUrls: ['./error-dialog.component.scss']
})
export class ErrorDialogComponent implements OnInit {
  title: string;
  message: string;
  constructor(
    public dialogRef: MatDialogRef<ErrorDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.title = MemberAssistanceMessages.TITLE_INTERNAL_SERVER_ERROR;
    this.message = data.message;
  }
  ngOnInit(): void {
    if(this.data.title) {
      this.title = this.data.title;
    }
  }
}
