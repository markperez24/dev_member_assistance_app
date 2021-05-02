import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HospitalReportsComponent } from './hospital-reports.component';
import { AuthGuard } from '../../../shared/guard/authguard.service';


const routes: Routes = [
  {
    path: '',
    component: HospitalReportsComponent
  }
];

@NgModule({
  providers: [AuthGuard],
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HospitalReportsRoutingModule {
}
