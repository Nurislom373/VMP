import {TestBed} from '@angular/core/testing';

import {CategoryStatusBadgeService} from "./category.status.badge.service";

describe('CategoryStatusBadgeService', () => {
  let service: CategoryStatusBadgeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CategoryStatusBadgeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
