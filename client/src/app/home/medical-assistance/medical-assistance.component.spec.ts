import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicalAssistanceComponent } from './medical-assistance.component';

describe('AssistanceApplicationsComponent', () => {
  let component: MedicalAssistanceComponent;
  let fixture: ComponentFixture<MedicalAssistanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MedicalAssistanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicalAssistanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
