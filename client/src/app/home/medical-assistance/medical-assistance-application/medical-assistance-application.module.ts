import { FlexLayoutModule } from '@angular/flex-layout';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MedicalAssistanceApplicationRoutingModule } from './medical-assistance-application-routing.module';
import { MedicalAssistanceApplicationComponent } from './medical-assistance-application.component';
import { SharedModule } from '../../../shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MedicalAssistanceApplicationRoutingModule,
    FlexLayoutModule.withConfig({addFlexToParent: false})
  ],
  declarations: [
    MedicalAssistanceApplicationComponent
  ]
})
export class MedicalAssistanceApplicationModule {
}
