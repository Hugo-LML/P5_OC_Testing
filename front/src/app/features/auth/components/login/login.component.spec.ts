import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const mockAuthService = {
    login: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  const mockSessionService = {
    logIn: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        { provide: SessionService, useValue: mockSessionService }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('submit', () => {
    it('should log in and navigate on successful login', () => {
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'test123',
      };
      const sessionInformation = {
        admin: true,
        id: 1
      };

      const spyLogin = jest.spyOn(mockAuthService, 'login').mockReturnValue(of(sessionInformation));

      component.form.setValue(loginRequest);
      component.submit();

      expect(spyLogin).toHaveBeenCalledWith(loginRequest);
      expect(mockSessionService.logIn).toHaveBeenCalledWith(sessionInformation);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
      expect(component.onError).toBeFalsy();
    });

    it('should set onError to true on login error', () => {
      const spyLogin = jest.spyOn(mockAuthService, 'login').mockReturnValue(throwError(() => new Error('Incorrect value(s)')));

      component.submit();

      expect(spyLogin).toHaveBeenCalled();
      expect(component.onError).toBeTruthy();
    });
  });

});
