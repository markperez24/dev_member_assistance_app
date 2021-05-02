import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportTypeSelectionDialogComponent } from './report-type-selection-dialog.component';

describe('ReportTypeSelectionDialogComponent', () => {
  let component: ReportTypeSelectionDialogComponent;
  let fixture: ComponentFixture<ReportTypeSelectionDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportTypeSelectionDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportTypeSelectionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
