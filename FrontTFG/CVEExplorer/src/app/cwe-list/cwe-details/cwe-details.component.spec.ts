import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CweDetailsComponent } from './cwe-details.component';

describe('CweDetailsComponent', () => {
  let component: CweDetailsComponent;
  let fixture: ComponentFixture<CweDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CweDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CweDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
