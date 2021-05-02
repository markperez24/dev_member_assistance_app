import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MembersRoutingModule } from './members-routing.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MembersComponent } from './members.component';
import { SharedModule } from '../../shared/shared.module';
import { DenyDialogComponent } from './deny-dialog/deny-dialog.component';
import { ApproveDialogComponent } from './approve-dialog/approve-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MembersRoutingModule,
    FlexLayoutModule.withConfig({addFlexToParent: false})
  ],
  declarations: [ MembersComponent, DenyDialogComponent, ApproveDialogComponent ]
})
export class MembersModule {

}
