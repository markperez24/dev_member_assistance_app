import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { CommonService } from "../../../shared/services/common.service";

@Component({
  selector: 'app-topnavadmin',
  templateUrl: './top-nav-admin.component.html',
  styleUrls: ['./top-nav-admin.component.scss']
})
export class TopNavAdminComponent implements OnInit {
  //private pushRightClass: any;
  mobile: boolean;
  displayName: any;
  access: any;

  constructor(public router: Router,
              private translate: TranslateService,
              private keycloakService:KeycloakService,
              private commonService: CommonService) {
    this.mobile = false;
    /*this.router.events.subscribe(val => {
      if (val instanceof NavigationEnd && window.innerWidth <= 992 && this.isToggled()) {
        //this.toggleSidebar();
      }
    });*/
  }

  ngOnInit() {
    //checks if on mobile
    if (window.screen.width < 768) { // 768px portrait
      this.mobile = true;
    }

    this.displayName = this.commonService.getNavDisplay(this.keycloakService.getUserRoles());
    this.access = this.commonService.getActionAccess();
    //this.pushRightClass = 'push-right';
  }

  /*isToggled(): boolean {
    const dom: HTMLBodyElement | null = document.querySelector('body');
    if (dom != null)
      return dom.classList.contains(this.pushRightClass);
    else
      return false;
  }*/

  /*toggleSidebar() {
    const dom: any = document.querySelector('body');
    dom.classList.toggle(this.pushRightClass);
  }*/

  logout() {
    localStorage.removeItem('isLoggedIn');
    this.keycloakService.logout();
  }

  /*changeLang(language: string) {
    this.translate.use(language);
  }*/
}
