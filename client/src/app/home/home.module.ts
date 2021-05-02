import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { TopNavComponent } from './layout/top-nav/top-nav.component';
import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { NavComponent } from './nav/nav.component';
import { RoleAccessModule } from '../shared/role-access/role-access.module';
import { SharedModule } from '../shared/shared.module';
import { NgxImageCompressService } from 'ngx-image-compress';
import { TopNavAdminComponent } from './layout/top-nav-admin/top-nav-admin.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HomeRoutingModule,
    RoleAccessModule,
    TranslateModule
  ],
  declarations: [
    HomeComponent,
    NavComponent,
    TopNavComponent,
    TopNavAdminComponent
  ],
  providers: [
    NgxImageCompressService
  ]
})
export class HomeModule {
}
