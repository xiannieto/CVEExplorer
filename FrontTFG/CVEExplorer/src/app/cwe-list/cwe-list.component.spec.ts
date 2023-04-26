import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CweListComponent } from './cwe-list.component';

describe('CweListComponent', () => {
  let component: CweListComponent;
  let fixture: ComponentFixture<CweListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CweListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CweListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
