jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EffortFaService } from '../service/effort-fa.service';
import { IEffortFa, EffortFa } from '../effort-fa.model';
import { IActivityFa } from 'app/entities/activity-fa/activity-fa.model';
import { ActivityFaService } from 'app/entities/activity-fa/service/activity-fa.service';

import { EffortFaUpdateComponent } from './effort-fa-update.component';

describe('Component Tests', () => {
  describe('EffortFa Management Update Component', () => {
    let comp: EffortFaUpdateComponent;
    let fixture: ComponentFixture<EffortFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let effortService: EffortFaService;
    let activityService: ActivityFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EffortFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EffortFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EffortFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      effortService = TestBed.inject(EffortFaService);
      activityService = TestBed.inject(ActivityFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ActivityFa query and add missing value', () => {
        const effort: IEffortFa = { id: 456 };
        const activity: IActivityFa = { id: 61469 };
        effort.activity = activity;

        const activityCollection: IActivityFa[] = [{ id: 81406 }];
        jest.spyOn(activityService, 'query').mockReturnValue(of(new HttpResponse({ body: activityCollection })));
        const additionalActivityFas = [activity];
        const expectedCollection: IActivityFa[] = [...additionalActivityFas, ...activityCollection];
        jest.spyOn(activityService, 'addActivityFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ effort });
        comp.ngOnInit();

        expect(activityService.query).toHaveBeenCalled();
        expect(activityService.addActivityFaToCollectionIfMissing).toHaveBeenCalledWith(activityCollection, ...additionalActivityFas);
        expect(comp.activitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const effort: IEffortFa = { id: 456 };
        const activity: IActivityFa = { id: 77915 };
        effort.activity = activity;

        activatedRoute.data = of({ effort });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(effort));
        expect(comp.activitiesSharedCollection).toContain(activity);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EffortFa>>();
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
        const saveSubject = new Subject<HttpResponse<EffortFa>>();
        const effort = new EffortFa();
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
        const saveSubject = new Subject<HttpResponse<EffortFa>>();
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
      describe('trackActivityFaById', () => {
        it('Should return tracked ActivityFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackActivityFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
