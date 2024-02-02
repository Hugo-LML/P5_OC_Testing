import { TestBed } from "@angular/core/testing";
import { AuthService } from "./auth.service";
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';
import { RegisterRequest } from "../interfaces/registerRequest.interface";
import { LoginRequest } from "../interfaces/loginRequest.interface";
import { SessionInformation } from "src/app/interfaces/sessionInformation.interface";

describe('Auth service', () => {
  let authService: AuthService;
  let httpTestingController: HttpTestingController;
  const pathService = 'api/auth';

  const mockAuthService = {
    register: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [AuthService],
      imports: [HttpClientTestingModule]
    })
      .compileComponents();

    authService = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(authService).toBeTruthy();
  });

  describe('register', () => {
    it('should send a POST request to register', () => {
      const registerRequest: RegisterRequest = {
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'test123',
      };

      authService.register(registerRequest).subscribe();

      const req = httpTestingController.expectOne(`${pathService}/register`);
      expect(req.request.method).toEqual('POST');
      expect(req.request.body).toEqual(registerRequest);

      req.flush(null);
    });
  });

  describe('login', () => {
    it('should send a POST request to login', () => {
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'test123',
      };

      const sessionInformation = {
        admin: true,
        id: 1
      };

      authService.login(loginRequest).subscribe(response => {
        expect(response).toEqual(sessionInformation);
      });

      const req = httpTestingController.expectOne(`${pathService}/login`);
      expect(req.request.method).toEqual('POST');
      expect(req.request.body).toEqual(loginRequest);

      req.flush(sessionInformation);
    });
  });
});