import { TestBed } from '@angular/core/testing';

import { CveService } from './cve-service.service';

describe('CveService', () => {
  let service: CveService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CveService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
