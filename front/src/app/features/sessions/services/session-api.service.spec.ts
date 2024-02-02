import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;
  const pathService = 'api/session';

  const mockSession = {
    id: 1,
    name: 'mySession1',
  }

  const mockSessions = [
    { id: 1, name: 'mySession1' },
    { id: 2, name: 'mySession2' },
    { id: 3, name: 'mySession3' },
  ]

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve sessions from the API via GET', () => {
    service.all().subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpTestingController.expectOne(pathService);
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should retrieve session details from the API via GET', () => {
    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne(`${pathService}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete a session via DELETE', () => {
    service.delete('1').subscribe();

    const req = httpTestingController.expectOne(`${pathService}/1`);
    expect(req.request.method).toBe('DELETE');
  });

  it('should create a session via POST', () => {
    service.create(mockSession as Session).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne(`${pathService}`);
    expect(req.request.method).toBe('POST');
    req.flush(mockSession);
  });

  it('should update a session via PUT', () => {
    const mockSessionUpdated = { id: 1, name: 'mySession1Updated' };

    service.update('1', mockSessionUpdated as Session).subscribe((session) => {
      expect(session).toEqual(mockSessionUpdated);
    });

    const req = httpTestingController.expectOne(`${pathService}/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockSessionUpdated);
  });

  it('should participate in a session via POST', () => {
    service.participate('1', 'user1').subscribe();

    const req = httpTestingController.expectOne(`${pathService}/1/participate/user1`);
    expect(req.request.method).toBe('POST');
  });

  it('should unparticipate in a session via DELETE', () => {
    service.unParticipate('1', 'user1').subscribe();

    const req = httpTestingController.expectOne(`${pathService}/1/participate/user1`);
    expect(req.request.method).toBe('DELETE');
  });
});
