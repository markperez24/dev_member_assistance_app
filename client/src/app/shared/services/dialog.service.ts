import { Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { SuccessDialogComponent } from '../dialogs/success-dialog/success-dialog.component';
import { map, take } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { ConfirmDialogComponent } from '../dialogs/confirm-dialog/confirm-dialog.component';
import { CancelDialogComponent } from '../dialogs/cancel-dialog/cancel-dialog.component';
//import { SpinnerComponent } from '../component/spinner/spinner-dialog.component';
import { AddHospitalDialogComponent } from '../../home/hospital/add-hospital-dialog/add-hospital-dialog.component';
import { AddBudgetDialogComponent } from '../../home/hospital/hospital-details/add-budget-dialog/add-budget-dialog.component';
import { ShowBudgetHistoryDialogComponent } from '../../home/hospital/hospital-details/show-budget-history-dialog/show-budget-history-dialog.component';

@Injectable({providedIn: 'root'})
export class DialogService {
  //loadingDialogDef: MatDialogRef<SpinnerComponent> | undefined;
  sucessDialogRef: MatDialogRef<SuccessDialogComponent> | undefined;
  confirmDialogRef: MatDialogRef<ConfirmDialogComponent> | undefined;
  cancelDialogRef: MatDialogRef<CancelDialogComponent> | undefined;
  addHospitalDialogRef: MatDialogRef<AddHospitalDialogComponent> | undefined;
  addBudgetDialogRef: MatDialogRef<AddBudgetDialogComponent> | undefined;
  showBudgetHistoryDialogRef: MatDialogRef<ShowBudgetHistoryDialogComponent> | undefined;

  constructor(private dialog: MatDialog) {
  }

  open(options: any) {
    this.sucessDialogRef = this.dialog.open(SuccessDialogComponent, {
      data: options
    });
    return this.sucessDialogRef;
  }

  openConfirmDialog(options: any) {
    this.confirmDialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: options
    });
  }

  openCancelDialog(options: any) {
    this.cancelDialogRef = this.dialog.open(CancelDialogComponent, {
      data: options
    });
  }

  /*showSpinner(options: any) {
    this.loadingDialogDef = this.dialog.open(SpinnerComponent, {
      panelClass: 'transparent',
      disableClose: true,
      data: options
    });
    return this.loadingDialogDef;
  }*/

  close(): Observable<any> {
    if (this.sucessDialogRef) {
      return this.sucessDialogRef.afterClosed()
        .pipe(take(1),
          map(res => {
              return res;
            }
          ));
    } else {
      return new Observable<any>();
    }
  }

  closeConfirm(): Observable<any> {
    if (this.confirmDialogRef) {
      return this.confirmDialogRef.afterClosed()
        .pipe(take(1),
          map(res => {
              return res;
            }
          ));
    } else {
      return new Observable<any>();
    }
  }

  closeCancel(): Observable<any> {
    if (this.cancelDialogRef) {
      return this.cancelDialogRef.afterClosed()
        .pipe(take(1),
          map(res => {
              return res;
            }
          ));
    } else {
      return new Observable<any>();
    }
  }

  public openAddHospitalDialog(options: any) {
    this.addHospitalDialogRef = this.dialog.open(AddHospitalDialogComponent, {
      width:'500px'});
  }

  /* public openAddBudget(options: any) {
    this.addBudgetDialogRef = this.dialog.open(AddBudgetDialogComponent, {
      width:'350px',
      data: options
    });
  }

  public openShowBudgetHistory(options: any) {
    this.showBudgetHistoryDialogRef = this.dialog.open(ShowBudgetHistoryDialogComponent, {
      width:'450px',
      height:'700px',
      data: options
    });
  } */
}
