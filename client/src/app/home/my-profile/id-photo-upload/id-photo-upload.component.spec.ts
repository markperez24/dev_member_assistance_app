import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IdPhotoUploadComponent } from './id-photo-upload.component';

describe('IdPhotoUploadComponent', () => {
  let component: IdPhotoUploadComponent;
  let fixture: ComponentFixture<IdPhotoUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IdPhotoUploadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IdPhotoUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
