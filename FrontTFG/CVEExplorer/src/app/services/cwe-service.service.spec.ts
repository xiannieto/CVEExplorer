import { TestBed } from '@angular/core/testing';

import { CweServiceService } from './cwe-service.service';

describe('CweServiceService', () => {
  let service: CweServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CweServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
