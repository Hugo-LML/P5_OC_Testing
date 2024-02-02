import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;
  const pathService = 'api/user';

  const mockUser = { id: 1, email: 'test@example.com' };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule,
      ]
    });
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve user details from the API via GET', () => {
    const userId = 1;

    service.getById(userId.toString()).subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpTestingController.expectOne(`${pathService}/${userId.toString()}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it('should delete user via DELETE', () => {
    const userId = 1;

    service.delete(userId.toString()).subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpTestingController.expectOne(`${pathService}/${userId.toString()}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
