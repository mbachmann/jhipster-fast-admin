jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ActivityFaService } from '../service/activity-fa.service';
import { IActivityFa, ActivityFa } from '../activity-fa.model';
import { ICatalogServiceFa } from 'app/entities/catalog-service-fa/catalog-service-fa.model';
import { CatalogServiceFaService } from 'app/entities/catalog-service-fa/service/catalog-service-fa.service';

import { ActivityFaUpdateComponent } from './activity-fa-update.component';

describe('Component Tests', () => {
  describe('ActivityFa Management Update Component', () => {
    let comp: ActivityFaUpdateComponent;
    let fixture: ComponentFixture<ActivityFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let activityService: ActivityFaService;
    let catalogServiceService: CatalogServiceFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ActivityFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ActivityFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ActivityFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      activityService = TestBed.inject(ActivityFaService);
      catalogServiceService = TestBed.inject(CatalogServiceFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CatalogServiceFa query and add missing value', () => {
        const activity: IActivityFa = { id: 456 };
        const activity: ICatalogServiceFa = { id: 79750 };
        activity.activity = activity;

        const catalogServiceCollection: ICatalogServiceFa[] = [{ id: 93379 }];
        jest.spyOn(catalogServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogServiceCollection })));
        const additionalCatalogServiceFas = [activity];
        const expectedCollection: ICatalogServiceFa[] = [...additionalCatalogServiceFas, ...catalogServiceCollection];
        jest.spyOn(catalogServiceService, 'addCatalogServiceFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ activity });
        comp.ngOnInit();

        expect(catalogServiceService.query).toHaveBeenCalled();
        expect(catalogServiceService.addCatalogServiceFaToCollectionIfMissing).toHaveBeenCalledWith(
          catalogServiceCollection,
          ...additionalCatalogServiceFas
        );
        expect(comp.catalogServicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const activity: IActivityFa = { id: 456 };
        const activity: ICatalogServiceFa = { id: 3726 };
        activity.activity = activity;

        activatedRoute.data = of({ activity });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(activity));
        expect(comp.catalogServicesSharedCollection).toContain(activity);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ActivityFa>>();
        const activity = { id: 123 };
        jest.spyOn(activityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ activity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: activity }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(activityService.update).toHaveBeenCalledWith(activity);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ActivityFa>>();
        const activity = new ActivityFa();
        jest.spyOn(activityService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ activity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: activity }));
        saveSubject.complete();

        // THEN
        expect(activityService.create).toHaveBeenCalledWith(activity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ActivityFa>>();
        const activity = { id: 123 };
        jest.spyOn(activityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ activity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(activityService.update).toHaveBeenCalledWith(activity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCatalogServiceFaById', () => {
        it('Should return tracked CatalogServiceFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCatalogServiceFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
