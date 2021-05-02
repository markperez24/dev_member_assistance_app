import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HospitalDetailsComponent } from './hospital-details.component';
import { AuthGuard } from '../../../shared/guard/authguard.service';

const routes: Routes = [
  {
    path: '',
    component: HospitalDetailsComponent,
    canActivate: [ AuthGuard ],
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HospitalDetailsRoutingModule { }
