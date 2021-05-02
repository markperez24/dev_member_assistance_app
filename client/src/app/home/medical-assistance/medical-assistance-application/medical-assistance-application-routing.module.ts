import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MedicalAssistanceApplicationComponent } from './medical-assistance-application.component';
import { AuthGuard } from '../../../shared/guard/authguard.service';

const routes: Routes = [
  {
    path: '',
    component: MedicalAssistanceApplicationComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MedicalAssistanceApplicationRoutingModule {
}
