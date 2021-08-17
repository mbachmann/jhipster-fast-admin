jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EffortService } from '../service/effort.service';
import { IEffort, Effort } from '../effort.model';
import { IReportingActivity } from 'app/entities/reporting-activity/reporting-activity.model';
import { ReportingActivityService } from 'app/entities/reporting-activity/service/reporting-activity.service';

import { EffortUpdateComponent } from './effort-update.component';

describe('Component Tests', () => {
  describe('Effort Management Update Component', () => {
    let comp: EffortUpdateComponent;
    let fixture: ComponentFixture<EffortUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let effortService: EffortService;
    let reportingActivityService: ReportingActivityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EffortUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EffortUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EffortUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      effortService = TestBed.inject(EffortService);
      reportingActivityService = TestBed.inject(ReportingActivityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ReportingActivity query and add missing value', () => {
        const effort: IEffort = { id: 456 };
        const activity: IReportingActivity = { id: 62991 };
        effort.activity = activity;

        const reportingActivityCollection: IReportingActivity[] = [{ id: 92141 }];
        jest.spyOn(reportingActivityService, 'query').mockReturnValue(of(new HttpResponse({ body: reportingActivityCollection })));
        const additionalReportingActivities = [activity];
        const expectedCollection: IReportingActivity[] = [...additionalReportingActivities, ...reportingActivityCollection];
        jest.spyOn(reportingActivityService, 'addReportingActivityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ effort });
        comp.ngOnInit();

        expect(reportingActivityService.query).toHaveBeenCalled();
        expect(reportingActivityService.addReportingActivityToCollectionIfMissing).toHaveBeenCalledWith(
          reportingActivityCollection,
          ...additionalReportingActivities
        );
        expect(comp.reportingActivitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const effort: IEffort = { id: 456 };
        const activity: IReportingActivity = { id: 53310 };
        effort.activity = activity;

        activatedRoute.data = of({ effort });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(effort));
        expect(comp.reportingActivitiesSharedCollection).toContain(activity);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Effort>>();
        const effort = { id: 123 };
        jest.spyOn(effortService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ effort });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: effort }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(effortService.update).toHaveBeenCalledWith(effort);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Effort>>();
        const effort = new Effort();
        jest.spyOn(effortService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ effort });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: effort }));
        saveSubject.complete();

        // THEN
        expect(effortService.create).toHaveBeenCalledWith(effort);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Effort>>();
        const effort = { id: 123 };
        jest.spyOn(effortService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ effort });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(effortService.update).toHaveBeenCalledWith(effort);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackReportingActivityById', () => {
        it('Should return tracked ReportingActivity primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackReportingActivityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
