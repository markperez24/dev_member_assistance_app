import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MyErrorStateMatcher } from "../../../public/registration/registration.component";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'app-deny-dialog',
  templateUrl: './deny-dialog.component.html',
  styleUrls: ['./deny-dialog.component.scss']
})
export class DenyDialogComponent implements OnInit {
  form!: FormGroup;
  matcher = new MyErrorStateMatcher();
  constructor(public dialogRef: MatDialogRef<DenyDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      reason: ['', Validators.required]
    });
  }

  close() {
    this.dialogRef.close();
  }

  onSubmit() {
    if(this.form.invalid) {
      return;
    }
    this.dialogRef.close({reason: this.form.controls.reason.value})
  }
}
