import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberReportsComponent } from './member-reports.component';

describe('MemberReportsComponent', () => {
  let component: MemberReportsComponent;
  let fixture: ComponentFixture<MemberReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MemberReportsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MemberReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
