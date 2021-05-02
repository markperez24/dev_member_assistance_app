import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationVideoComponent } from './registration-video.component';

describe('RegistrationVideoComponent', () => {
  let component: RegistrationVideoComponent;
  let fixture: ComponentFixture<RegistrationVideoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegistrationVideoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationVideoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
