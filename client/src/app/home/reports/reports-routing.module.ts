import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../../shared/guard/authguard.service';
import { ReportsComponent } from "./reports.component";

const routes: Routes = [
  {
    path: '',
    component: ReportsComponent,
    children: [
      {
        path: 'member-reports',
        //outlet: 'member-reports',
        loadChildren: () => import('./member-reports/member-reports.module').then(m => m.MemberReportsModule),
        canActivate: [ AuthGuard ],
        data: {roles: ['Administrator']}
      },
      {
        path: 'hospital-reports',
        //outlet: 'member-reports',
        loadChildren: () => import('./hospital-reports/hospital-reports.module').then(m => m.HospitalReportsModule),
        canActivate: [ AuthGuard ],
        data: {roles: ['Administrator']}
      },
    ]
  }
];

@NgModule({
  providers: [AuthGuard],
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ReportsRoutingModule {
}
