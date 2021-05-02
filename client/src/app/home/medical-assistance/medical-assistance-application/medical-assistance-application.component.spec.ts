import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicalAssistanceApplicationComponent } from './medical-assistance-application.component';

describe('MedicalAssistanceApplicationComponent', () => {
  let component: MedicalAssistanceApplicationComponent;
  let fixture: ComponentFixture<MedicalAssistanceApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MedicalAssistanceApplicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicalAssistanceApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
