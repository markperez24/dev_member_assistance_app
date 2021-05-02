import { Component, Inject, OnInit } from '@angular/core';
import { CommonService } from '../../services/common.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent implements OnInit {
  message: string;
  title: string;

  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.title = data.title;
    this.message = data.message;
  }
  ngOnInit(): void {
  }

  cancel() {
      this.dialogRef.close();
  }

  continue() {
      this.dialogRef.close({continue : true});
  }
}
