import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormControl } from "@angular/forms";

@Component({
  selector: 'app-report-type-selection-dialog',
  templateUrl: './report-type-selection-dialog.component.html',
  styleUrls: ['./report-type-selection-dialog.component.scss']
})
export class ReportTypeSelectionDialogComponent implements OnInit {
  reportType: FormControl;

  constructor(public dialogRef: MatDialogRef<ReportTypeSelectionDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.reportType = new FormControl();
  }

  ngOnInit(): void {
    this.reportType.setValue('xlsx');
  }

  close() {
    this.dialogRef.close();
  }

  generate() {
    this.dialogRef.close({type: this.reportType.value});
  }
}
