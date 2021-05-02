import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { VerificationRoutingModule } from './verification-routing.module';
import { VerificationComponent } from './verification.component';

@NgModule({
  imports: [
    CommonModule,
    VerificationRoutingModule,
    SharedModule,
    FlexLayoutModule.withConfig({addFlexToParent: false})
  ],
  declarations: [
    VerificationComponent
  ]
})
export class VerificationModule { }
