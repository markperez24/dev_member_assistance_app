import { Directive, ElementRef, Input, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { RoleService } from '../services/role.service';

@Directive({
  selector: '[accessRights]'
})
export class RoleAccessComponent implements OnInit {
  @Input() accessRights: any;

  constructor(
    private elem: ElementRef,
    private templateRef: TemplateRef<any>,
    private viewContainerRef: ViewContainerRef,
    private roleService: RoleService
  ) {
  }

  ngOnInit() {
    const hasAccess: boolean = this.roleService.hasAccess(this.accessRights);
    if (hasAccess) {
      this.viewContainerRef.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainerRef.clear();
    }
  }
}
