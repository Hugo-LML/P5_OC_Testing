import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule, MatCardTitle } from '@angular/material/card';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;

  const mockSession: Session = {
    id: 1,
    name: '',
    description: '',
    date: new Date(2024, 0, 29),
    teacher_id: 1,
    users: [],
    createdAt: new Date(2024, 0, 29),
    updatedAt: new Date(2024, 0, 29),
  }

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn(),
      },
    },
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockSessionApiService = {
    delete: jest.fn(),
    participate: jest.fn(),
    unParticipate: jest.fn(),
    detail: jest.fn().mockReturnValue(of(mockSession)),
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockSession)),
  }
  
  const mockSnackBar = {
    open: jest.fn(),
  };
  
  const mockRouter = {
    navigate: jest.fn(),
  };


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatIconModule,
        MatCardModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: mockRouter },
      ],
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate back when back() is called', () => {
    const spyBack = jest.spyOn(window.history, 'back');
    component.back();
    expect(spyBack).toHaveBeenCalled();
  });

  it('should call delete and navigate to sessions when delete() is called', () => {
    const spyDelete = jest.spyOn(mockSessionApiService, 'delete').mockReturnValue(of(null));
    
    component.delete();

    expect(spyDelete).toHaveBeenCalledWith(component.sessionId);
    expect(mockSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call participate when participate() is called', () => {
    const spyParticipate = jest.spyOn(mockSessionApiService, 'participate').mockReturnValue(of(null));
    const spyFetchSession = jest.spyOn(component as any, 'fetchSession');

    component.participate();

    expect(spyParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(spyFetchSession).toHaveBeenCalled();
  });

  it('should call unParticipate when unParticipate() is called', () => {
    const spyUnParticipate = jest.spyOn(mockSessionApiService, 'unParticipate').mockReturnValue(of(null));
    const spyFetchSession = jest.spyOn(component as any, 'fetchSession');

    component.unParticipate();

    expect(spyUnParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(spyFetchSession).toHaveBeenCalled();
  });
  
  it('should fetch session details on ngOnInit', () => {
    const spyFetchSession = jest.spyOn(component as any, 'fetchSession');

    component.ngOnInit();

    expect(spyFetchSession).toHaveBeenCalled();
  });
});

