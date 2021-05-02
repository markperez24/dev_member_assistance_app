import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HospitalComponent } from "./hospital.component";
import { HospitalRoutingModule } from "./hospital-routing.module";
import { AddHospitalDialogComponent } from './add-hospital-dialog/add-hospital-dialog.component';
import { SharedModule } from "../../shared/shared.module";


@NgModule({
  declarations: [HospitalComponent, AddHospitalDialogComponent],
  imports: [
    CommonModule,
    HospitalRoutingModule,
    FlexLayoutModule.withConfig({addFlexToParent: false}),
    SharedModule
  ],
  exports : [
    AddHospitalDialogComponent
  ],
})
export class HospitalModule {
}
