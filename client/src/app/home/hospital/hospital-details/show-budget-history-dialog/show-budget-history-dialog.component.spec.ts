import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowBudgetHistoryDialogComponent } from './show-budget-history-dialog.component';

describe('ShowBudgetHistoryDialogComponent', () => {
  let component: ShowBudgetHistoryDialogComponent;
  let fixture: ComponentFixture<ShowBudgetHistoryDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowBudgetHistoryDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowBudgetHistoryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
