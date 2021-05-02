import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';
import { ReportsRoutingModule } from './reports-routing.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ReportsComponent } from './reports.component';
import { ReportTypeSelectionDialogComponent } from './report-type-selection-dialog/report-type-selection-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    ReportsRoutingModule,
    FlexLayoutModule.withConfig({addFlexToParent: false})
  ],
  declarations: [
    ReportsComponent,
    ReportTypeSelectionDialogComponent
  ]
})
export class ReportsModule {
}
