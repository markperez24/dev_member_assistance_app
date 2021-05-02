import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../../shared/shared.module';
import { MemberReportsRoutingModule } from './member-reports-routing.module';
import { FlexLayoutModule } from "@angular/flex-layout";
import { MemberReportsComponent } from "./member-reports.component";

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MemberReportsRoutingModule,
    FlexLayoutModule.withConfig({addFlexToParent: false})
  ],
  declarations: [ MemberReportsComponent ]
})
export class MemberReportsModule {
}
