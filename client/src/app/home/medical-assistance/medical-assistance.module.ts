import { FlexLayoutModule } from '@angular/flex-layout';
import { MedicalAssistanceRoutingModule } from './medical-assistance-routing.module';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MedicalAssistanceComponent } from './medical-assistance.component';
import { SharedModule } from "../../shared/shared.module";

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MedicalAssistanceRoutingModule,
    FlexLayoutModule.withConfig({addFlexToParent: false})
  ],
  declarations: [
    MedicalAssistanceComponent
  ]
})
export class MedicalAssistanceModule {
}
