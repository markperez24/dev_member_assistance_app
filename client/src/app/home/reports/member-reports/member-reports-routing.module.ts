import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MemberReportsComponent } from './member-reports.component';
import { AuthGuard } from '../../../shared/guard/authguard.service';

const routes: Routes = [
  {
    path: '',
    component: MemberReportsComponent
  }
];

@NgModule({
  providers: [AuthGuard],
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MemberReportsRoutingModule {
}
