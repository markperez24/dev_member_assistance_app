import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HospitalComponent } from './hospital.component';
//import { HospitalDetailsComponent } from './hospital-details/hospital-details.component';

const routes: Routes = [
  {
    path: '',
    component: HospitalComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HospitalRoutingModule {}
