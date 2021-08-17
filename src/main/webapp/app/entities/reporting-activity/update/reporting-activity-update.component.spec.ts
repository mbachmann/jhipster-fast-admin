jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReportingActivityService } from '../service/reporting-activity.service';
import { IReportingActivity, ReportingActivity } from '../reporting-activity.model';
import { ICatalogService } from 'app/entities/catalog-service/catalog-service.model';
import { CatalogServiceService } from 'app/entities/catalog-service/service/catalog-service.service';

import { ReportingActivityUpdateComponent } from './reporting-activity-update.component';

describe('Component Tests', () => {
  describe('ReportingActivity Management Update Component', () => {
    let comp: ReportingActivityUpdateComponent;
    let fixture: ComponentFixture<ReportingActivityUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let reportingActivityService: ReportingActivityService;
    let catalogServiceService: CatalogServiceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReportingActivityUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReportingActivityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReportingActivityUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      reportingActivityService = TestBed.inject(ReportingActivityService);
      catalogServiceService = TestBed.inject(CatalogServiceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CatalogService query and add missing value', () => {
        const reportingActivity: IReportingActivity = { id: 456 };
        const catalogService: ICatalogService = { id: 84313 };
        reportingActivity.catalogService = catalogService;

        const catalogServiceCollection: ICatalogService[] = [{ id: 13757 }];
        jest.spyOn(catalogServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogServiceCollection })));
        const additionalCatalogServices = [catalogService];
        const expectedCollection: ICatalogService[] = [...additionalCatalogServices, ...catalogServiceCollection];
        jest.spyOn(catalogServiceService, 'addCatalogServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ reportingActivity });
        comp.ngOnInit();

        expect(catalogServiceService.query).toHaveBeenCalled();
        expect(catalogServiceService.addCatalogServiceToCollectionIfMissing).toHaveBeenCalledWith(
          catalogServiceCollection,
          ...additionalCatalogServices
        );
        expect(comp.catalogServicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const reportingActivity: IReportingActivity = { id: 456 };
        const catalogService: ICatalogService = { id: 69050 };
        reportingActivity.catalogService = catalogService;

        activatedRoute.data = of({ reportingActivity });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(reportingActivity));
        expect(comp.catalogServicesSharedCollection).toContain(catalogService);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ReportingActivity>>();
        const reportingActivity = { id: 123 };
        jest.spyOn(reportingActivityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reportingActivity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reportingActivity }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(reportingActivityService.update).toHaveBeenCalledWith(reportingActivity);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ReportingActivity>>();
        const reportingActivity = new ReportingActivity();
        jest.spyOn(reportingActivityService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reportingActivity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reportingActivity }));
        saveSubject.complete();

        // THEN
        expect(reportingActivityService.create).toHaveBeenCalledWith(reportingActivity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ReportingActivity>>();
        const reportingActivity = { id: 123 };
        jest.spyOn(reportingActivityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reportingActivity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(reportingActivityService.update).toHaveBeenCalledWith(reportingActivity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCatalogServiceById', () => {
        it('Should return tracked CatalogService primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCatalogServiceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
