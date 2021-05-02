import { Component, OnInit } from '@angular/core';
import { KeycloakService } from "keycloak-angular";

@Component({
  selector: 'app-public',
  templateUrl: './public.component.html',
  styleUrls: ['./public.component.scss']
})
export class PublicComponent implements OnInit {
  constructor(private readonly keycloak: KeycloakService) { }
  ngOnInit(): void {
    this.login();
  }

  login() {
    this.keycloak.login({
      redirectUri: 'http://localhost:4200/home'
    });
  }
}
