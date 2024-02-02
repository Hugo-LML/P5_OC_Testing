import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  
  const mockSession: Session = {
    id: 1,
    name: 'mySession',
    description: '',
    date: new Date(),
    teacher_id: 1,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date(),
  }

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn(),
      },
    },
  };

  const mockMatSnackBar = {
    open: jest.fn(),
  };

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of({ ...mockSession, name: 'mySessionDetail' })),
    create: jest.fn().mockReturnValue(of({ ...mockSession, name: 'mySessionCreated' })),
    update: jest.fn().mockReturnValue(of({ ...mockSession, name: 'mySessionUpdated' })),
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const mockTeacherService = {
    all: jest.fn(),
  };

  const mockRouter = {
    url: '',
    navigate: jest.fn(),
  };
  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should redirect to /sessions if the user is not an admin', () => {
      mockSessionService.sessionInformation.admin = false;

      component.ngOnInit();

      expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    });

    it('should only initialize form if url does not inclue update', () => {
      const spyInitForm = jest.spyOn(component as any, 'initForm');

      component.ngOnInit();

      expect(spyInitForm).toHaveBeenCalled();
    });

    it("should initialize form with session's data in update mode", () => {
      component.onUpdate = false;
      mockRouter.url = '/update/1';
      const spyGet = jest.spyOn(mockActivatedRoute.snapshot.paramMap, 'get');

      component.ngOnInit();

      expect(component.onUpdate).toBe(true);
      expect(spyGet).toHaveBeenCalled();
    });
  });

  describe('initForm', () => {
    it("should initialize form with session's data", () => {
      component['initForm'](mockSession);

      const formValue = component.sessionForm?.getRawValue();

      expect(formValue.name).toEqual(mockSession.name);
    });

    it('should initialize form with default values if no session provided', () => {
      component['initForm']();

      const formValue = component.sessionForm?.value;

      expect(formValue).toEqual({
        name: '',
        date: '',
        teacher_id: '',
        description: '',
      });
    });
  });

  describe('exitPage', () => {
    it('should call matSnackBar.open and navigate to /sessions', () => {
      const spyOpen = jest.spyOn(mockMatSnackBar, 'open');
      const spyNavigate = jest.spyOn(mockRouter, 'navigate');

      component['exitPage']('Test Message');

      expect(spyOpen).toHaveBeenCalledWith('Test Message', 'Close', { duration: 3000 });
      expect(spyNavigate).toHaveBeenCalledWith(['/sessions']);
    });
  });
});
