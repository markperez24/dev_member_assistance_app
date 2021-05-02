import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HospitalReportsComponent } from './hospital-reports.component';

describe('HospitalReportsComponent', () => {
  let component: HospitalReportsComponent;
  let fixture: ComponentFixture<HospitalReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HospitalReportsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HospitalReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
