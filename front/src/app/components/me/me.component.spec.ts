import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { of } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockUser: User = {
    id: 1,
    email: 'test@example.com',
    lastName: 'Doe',
    firstName: 'John',
    admin: true,
    password: '',
    createdAt: new Date(),
  }

  const mockRouter = {
    navigate: jest.fn()
  }

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  }

  const mockMatSnackBar = {
    open: jest.fn()
  }

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(mockUser)),
    delete: jest.fn(),
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: UserService, useValue: mockUserService },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should retrieve current user informations', () => {
    component.ngOnInit();
    expect(mockUserService.getById).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());
    expect(component.user).toEqual(mockUser);
  });

  it('should navigate back when back() is called', () => {
    const spyBack = jest.spyOn(window.history, 'back');
    component.back();
    expect(spyBack).toHaveBeenCalled();
  });

  it("should delete user's account", () => {
    const spyDelete = jest.spyOn(mockUserService, 'delete').mockReturnValue(of({}));
    const spyOpen = jest.spyOn(mockMatSnackBar, 'open');
    const spyLogOut = jest.spyOn(mockSessionService, 'logOut');
    const spyNavigate = jest.spyOn(mockRouter, 'navigate');
    component.delete();
    expect(spyDelete).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());
    expect(spyOpen).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(spyLogOut).toHaveBeenCalled();
    expect(spyNavigate).toHaveBeenCalledWith(['/']);
  });

});
