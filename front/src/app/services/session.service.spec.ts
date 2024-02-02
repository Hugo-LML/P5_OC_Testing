import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start with isLogged as false', () => {
    expect(service.isLogged).toBeFalsy();
  });

  it('should start with sessionInformation as undefined', () => {
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should set sessionInformation when logging in', () => {
    const mockSessionInformation: SessionInformation = {
      token: '',
      type: '',
      id: 1,
      username: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: true,
    };

    service.logIn(mockSessionInformation);
    expect(service.sessionInformation).toEqual(mockSessionInformation);
  });

  it('should set isLogged to true when logging in', () => {
    const mockSessionInformation: SessionInformation = {
      token: '',
      type: '',
      id: 1,
      username: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: true,
    };

    service.logIn(mockSessionInformation);
    expect(service.isLogged).toBeTruthy();
  });

  it('should set sessionInformation to undefined when logging out', () => {
    const mockSessionInformation: SessionInformation = {
      token: '',
      type: '',
      id: 1,
      username: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: true,
    };

    service.logIn(mockSessionInformation);
    service.logOut();
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should set isLogged to false when logging out', () => {
    const mockSessionInformation: SessionInformation = {
      token: '',
      type: '',
      id: 1,
      username: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: true,
    };

    service.logIn(mockSessionInformation);
    service.logOut();
    expect(service.isLogged).toBeFalsy();
  });
});
