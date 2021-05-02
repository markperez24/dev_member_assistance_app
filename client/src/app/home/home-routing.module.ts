import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home.component';
import { AuthGuard } from '../shared/guard/authguard.service';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    //canActivate: [ AuthGuard ],
    children: [
      /*{
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },*/
      /*{
        path: 'dashboard',
        loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule),
        canActivate: [ AuthGuard ],
        data: {roles: ['Administrator']}
      },*/
      {
        path: 'members',
        loadChildren: () => import('./members/members.module').then(m => m.MembersModule),
        canActivate: [AuthGuard],
        data: {roles: ['Administrator']}
      },
      {
        path: 'member-details',
        loadChildren: () => import('./members/member-details/member-details.module').then(m => m.MemberDetailsModule),
        canActivate: [AuthGuard],
        data: {roles: ['Administrator']}
      },
      {
        path: 'medical-assistance',
        loadChildren: () => import('./medical-assistance/medical-assistance.module').then(m => m.MedicalAssistanceModule),
        canActivate: [AuthGuard],
        data: {roles: ['Member']}
      },
      {
        path: 'my-profile',
        loadChildren: () => import('./my-profile/my-profile.module').then(m => m.MyProfileModule),
        canActivate: [AuthGuard],
        data: {roles: ['Member']}
      },
      {
        path: 'hospitals',
        loadChildren: () => import('./hospital/hospital.module').then(m => m.HospitalModule),
        canActivate: [AuthGuard],
        data: {roles: ['Administrator']}
      },
      {
        path: 'hospital-details',
        loadChildren: () => import('./hospital/hospital-details/hospital-details.module').then(m => m.HospitalDetailsModule),
        canActivate: [AuthGuard],
        data: {roles: ['Administrator']}
      },
      {
        path: 'medical-assistance-application',
        loadChildren: () => import('./medical-assistance/medical-assistance-application/medical-assistance-application.module').then(m => m.MedicalAssistanceApplicationModule),
        canActivate: [AuthGuard],
        data: {roles: ['Member']}
      },
      {
        path: 'reports',
        loadChildren: () => import('./reports/reports.module').then(m => m.ReportsModule),
        canActivate: [AuthGuard],
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
export class HomeRoutingModule {
}
