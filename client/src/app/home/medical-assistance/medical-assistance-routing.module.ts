import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MedicalAssistanceComponent } from './medical-assistance.component';

const routes: Routes = [
  {
    path: '',
    component: MedicalAssistanceComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MedicalAssistanceRoutingModule {}
