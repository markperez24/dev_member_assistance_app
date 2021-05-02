import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HospitalDetailsRoutingModule } from './hospital-details-routing.module';
import { HospitalDetailsComponent } from './hospital-details.component';
import { SharedModule } from '../../../shared/shared.module';
import { AddBudgetDialogComponent } from './add-budget-dialog/add-budget-dialog.component';
import { ShowBudgetHistoryDialogComponent } from './show-budget-history-dialog/show-budget-history-dialog.component';

@NgModule({
  declarations: [HospitalDetailsComponent, AddBudgetDialogComponent, ShowBudgetHistoryDialogComponent],
  imports: [
    CommonModule,
    SharedModule,
    HospitalDetailsRoutingModule,
    FlexLayoutModule.withConfig({addFlexToParent: false})
  ]
})
export class HospitalDetailsModule { }
