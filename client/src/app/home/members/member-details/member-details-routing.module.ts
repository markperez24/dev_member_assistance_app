import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MemberDetailsComponent } from './member-details.component';
import { AuthGuard } from '../../../shared/guard/authguard.service';


const routes: Routes = [
  {
    path: '',
    component: MemberDetailsComponent,
    canActivate: [ AuthGuard ],
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MemberDetailsRoutingModule { }
