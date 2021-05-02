import { Injectable } from '@angular/core';
import { AbstractService } from './abstract.service';
import { KeycloakService } from 'keycloak-angular';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { AddBudgetDialogComponent } from '../../home/hospital/hospital-details/add-budget-dialog/add-budget-dialog.component';
import { ShowBudgetHistoryDialogComponent } from '../../home/hospital/hospital-details/show-budget-history-dialog/show-budget-history-dialog.component';

@Injectable({providedIn: 'root'})
export class HospitalDetailsService extends AbstractService {
  addBudgetDialogRef: MatDialogRef<AddBudgetDialogComponent> | undefined;
  showBudgetHistoryDialogRef: MatDialogRef<ShowBudgetHistoryDialogComponent> | undefined;

  constructor(private dialog: MatDialog,
              private keycloakService: KeycloakService) {
    super();
  }

  /**
   * Open selected hospital's <Add Budget> dialog
   * @param options 
   */
  openAddBudget(options: any) {
    this.addBudgetDialogRef = this.dialog.open(AddBudgetDialogComponent, {
      width:'350px',
      data: options
    });
  }

  /**
   * Open selected hospital's <Show Budget Replenish History> dialog
   * @param options 
   */
  openShowBudgetHistory(options: any) {
    this.showBudgetHistoryDialogRef = this.dialog.open(ShowBudgetHistoryDialogComponent, {
      width:'450px',
      height:'700px',
      data: options
    });
  }

  hasAddBudgetAccess() {
    if(this.keycloakService.getUserRoles().indexOf('Super Administrator') > -1) {
      return true;
    }
    return false;
  }

  hasShowBudgetAccess() {
    let access = false;
    if(this.keycloakService.getUserRoles().indexOf('Administrator') > -1) {
      access = true;
    } else if(this.keycloakService.getUserRoles().indexOf('Super Administrator') > -1){
      access = true;
    }
    return access;
  }

}
