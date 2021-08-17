jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkingHourService } from '../service/working-hour.service';
import { IWorkingHour, WorkingHour } from '../working-hour.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { IEffort } from 'app/entities/effort/effort.model';
import { EffortService } from 'app/entities/effort/service/effort.service';

import { WorkingHourUpdateComponent } from './working-hour-update.component';

describe('Component Tests', () => {
  describe('WorkingHour Management Update Component', () => {
    let comp: WorkingHourUpdateComponent;
    let fixture: ComponentFixture<WorkingHourUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let workingHourService: WorkingHourService;
    let applicationUserService: ApplicationUserService;
    let effortService: EffortService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WorkingHourUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WorkingHourUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkingHourUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      workingHourService = TestBed.inject(WorkingHourService);
      applicationUserService = TestBed.inject(ApplicationUserService);
      effortService = TestBed.inject(EffortService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ApplicationUser query and add missing value', () => {
        const workingHour: IWorkingHour = { id: 456 };
        const applicationUser: IApplicationUser = { id: 98549 };
        workingHour.applicationUser = applicationUser;

        const applicationUserCollection: IApplicationUser[] = [{ id: 82500 }];
        jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
        const additionalApplicationUsers = [applicationUser];
        const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
        jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ workingHour });
        comp.ngOnInit();

        expect(applicationUserService.query).toHaveBeenCalled();
        expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
          applicationUserCollection,
          ...additionalApplicationUsers
        );
        expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Effort query and add missing value', () => {
        const workingHour: IWorkingHour = { id: 456 };
        const effort: IEffort = { id: 56886 };
        workingHour.effort = effort;

        const effortCollection: IEffort[] = [{ id: 44756 }];
        jest.spyOn(effortService, 'query').mockReturnValue(of(new HttpResponse({ body: effortCollection })));
        const additionalEfforts = [effort];
        const expectedCollection: IEffort[] = [...additionalEfforts, ...effortCollection];
        jest.spyOn(effortService, 'addEffortToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ workingHour });
        comp.ngOnInit();

        expect(effortService.query).toHaveBeenCalled();
        expect(effortService.addEffortToCollectionIfMissing).toHaveBeenCalledWith(effortCollection, ...additionalEfforts);
        expect(comp.effortsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const workingHour: IWorkingHour = { id: 456 };
        const applicationUser: IApplicationUser = { id: 71389 };
        workingHour.applicationUser = applicationUser;
        const effort: IEffort = { id: 89356 };
        workingHour.effort = effort;

        activatedRoute.data = of({ workingHour });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(workingHour));
        expect(comp.applicationUsersSharedCollection).toContain(applicationUser);
        expect(comp.effortsSharedCollection).toContain(effort);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WorkingHour>>();
        const workingHour = { id: 123 };
        jest.spyOn(workingHourService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ workingHour });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workingHour }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(workingHourService.update).toHaveBeenCalledWith(workingHour);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WorkingHour>>();
        const workingHour = new WorkingHour();
        jest.spyOn(workingHourService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ workingHour });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workingHour }));
        saveSubject.complete();

        // THEN
        expect(workingHourService.create).toHaveBeenCalledWith(workingHour);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WorkingHour>>();
        const workingHour = { id: 123 };
        jest.spyOn(workingHourService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ workingHour });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(workingHourService.update).toHaveBeenCalledWith(workingHour);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackApplicationUserById', () => {
        it('Should return tracked ApplicationUser primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApplicationUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEffortById', () => {
        it('Should return tracked Effort primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEffortById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
