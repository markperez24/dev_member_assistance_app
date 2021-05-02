import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { SharedModule } from '../../../shared/shared.module';

import { MemberDetailsRoutingModule } from './member-details-routing.module';
import { MemberDetailsComponent } from './member-details.component';


@NgModule({
  declarations: [MemberDetailsComponent],
  imports: [
    CommonModule,
    SharedModule,
    MemberDetailsRoutingModule,
    FlexLayoutModule.withConfig({addFlexToParent: false})
  ]
})
export class MemberDetailsModule { }
