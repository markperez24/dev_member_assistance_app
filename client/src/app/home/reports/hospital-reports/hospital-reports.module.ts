import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../../shared/shared.module';
import { FlexLayoutModule } from "@angular/flex-layout";
import { HospitalReportsRoutingModule } from './hospital-reports-routing.module';
import { HospitalReportsComponent } from './hospital-reports.component';


@NgModule({
  declarations: [HospitalReportsComponent],
  imports: [
    CommonModule,
    SharedModule,
    HospitalReportsRoutingModule,
    FlexLayoutModule.withConfig({addFlexToParent: false})
  ]
})
export class HospitalReportsModule {
}
