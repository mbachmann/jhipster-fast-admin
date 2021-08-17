jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CatalogServiceFaService } from '../service/catalog-service-fa.service';
import { ICatalogServiceFa, CatalogServiceFa } from '../catalog-service-fa.model';
import { ICatalogCategoryFa } from 'app/entities/catalog-category-fa/catalog-category-fa.model';
import { CatalogCategoryFaService } from 'app/entities/catalog-category-fa/service/catalog-category-fa.service';
import { ICatalogUnitFa } from 'app/entities/catalog-unit-fa/catalog-unit-fa.model';
import { CatalogUnitFaService } from 'app/entities/catalog-unit-fa/service/catalog-unit-fa.service';
import { IVatFa } from 'app/entities/vat-fa/vat-fa.model';
import { VatFaService } from 'app/entities/vat-fa/service/vat-fa.service';

import { CatalogServiceFaUpdateComponent } from './catalog-service-fa-update.component';

describe('Component Tests', () => {
  describe('CatalogServiceFa Management Update Component', () => {
    let comp: CatalogServiceFaUpdateComponent;
    let fixture: ComponentFixture<CatalogServiceFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let catalogServiceService: CatalogServiceFaService;
    let catalogCategoryService: CatalogCategoryFaService;
    let catalogUnitService: CatalogUnitFaService;
    let vatService: VatFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogServiceFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CatalogServiceFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatalogServiceFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      catalogServiceService = TestBed.inject(CatalogServiceFaService);
      catalogCategoryService = TestBed.inject(CatalogCategoryFaService);
      catalogUnitService = TestBed.inject(CatalogUnitFaService);
      vatService = TestBed.inject(VatFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CatalogCategoryFa query and add missing value', () => {
        const catalogService: ICatalogServiceFa = { id: 456 };
        const category: ICatalogCategoryFa = { id: 25203 };
        catalogService.category = category;

        const catalogCategoryCollection: ICatalogCategoryFa[] = [{ id: 66540 }];
        jest.spyOn(catalogCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogCategoryCollection })));
        const additionalCatalogCategoryFas = [category];
        const expectedCollection: ICatalogCategoryFa[] = [...additionalCatalogCategoryFas, ...catalogCategoryCollection];
        jest.spyOn(catalogCategoryService, 'addCatalogCategoryFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ catalogService });
        comp.ngOnInit();

        expect(catalogCategoryService.query).toHaveBeenCalled();
        expect(catalogCategoryService.addCatalogCategoryFaToCollectionIfMissing).toHaveBeenCalledWith(
          catalogCategoryCollection,
          ...additionalCatalogCategoryFas
        );
        expect(comp.catalogCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CatalogUnitFa query and add missing value', () => {
        const catalogService: ICatalogServiceFa = { id: 456 };
        const unit: ICatalogUnitFa = { id: 27797 };
        catalogService.unit = unit;

        const catalogUnitCollection: ICatalogUnitFa[] = [{ id: 84941 }];
        jest.spyOn(catalogUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogUnitCollection })));
        const additionalCatalogUnitFas = [unit];
        const expectedCollection: ICatalogUnitFa[] = [...additionalCatalogUnitFas, ...catalogUnitCollection];
        jest.spyOn(catalogUnitService, 'addCatalogUnitFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ catalogService });
        comp.ngOnInit();

        expect(catalogUnitService.query).toHaveBeenCalled();
        expect(catalogUnitService.addCatalogUnitFaToCollectionIfMissing).toHaveBeenCalledWith(
          catalogUnitCollection,
          ...additionalCatalogUnitFas
        );
        expect(comp.catalogUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call VatFa query and add missing value', () => {
        const catalogService: ICatalogServiceFa = { id: 456 };
        const vat: IVatFa = { id: 28609 };
        catalogService.vat = vat;

        const vatCollection: IVatFa[] = [{ id: 51704 }];
        jest.spyOn(vatService, 'query').mockReturnValue(of(new HttpResponse({ body: vatCollection })));
        const additionalVatFas = [vat];
        const expectedCollection: IVatFa[] = [...additionalVatFas, ...vatCollection];
        jest.spyOn(vatService, 'addVatFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ catalogService });
        comp.ngOnInit();

        expect(vatService.query).toHaveBeenCalled();
        expect(vatService.addVatFaToCollectionIfMissing).toHaveBeenCalledWith(vatCollection, ...additionalVatFas);
        expect(comp.vatsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const catalogService: ICatalogServiceFa = { id: 456 };
        const category: ICatalogCategoryFa = { id: 89106 };
        catalogService.category = category;
        const unit: ICatalogUnitFa = { id: 53669 };
        catalogService.unit = unit;
        const vat: IVatFa = { id: 28246 };
        catalogService.vat = vat;

        activatedRoute.data = of({ catalogService });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(catalogService));
        expect(comp.catalogCategoriesSharedCollection).toContain(category);
        expect(comp.catalogUnitsSharedCollection).toContain(unit);
        expect(comp.vatsSharedCollection).toContain(vat);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogServiceFa>>();
        const catalogService = { id: 123 };
        jest.spyOn(catalogServiceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogService });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalogService }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(catalogServiceService.update).toHaveBeenCalledWith(catalogService);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogServiceFa>>();
        const catalogService = new CatalogServiceFa();
        jest.spyOn(catalogServiceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogService });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalogService }));
        saveSubject.complete();

        // THEN
        expect(catalogServiceService.create).toHaveBeenCalledWith(catalogService);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogServiceFa>>();
        const catalogService = { id: 123 };
        jest.spyOn(catalogServiceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogService });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(catalogServiceService.update).toHaveBeenCalledWith(catalogService);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCatalogCategoryFaById', () => {
        it('Should return tracked CatalogCategoryFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCatalogCategoryFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCatalogUnitFaById', () => {
        it('Should return tracked CatalogUnitFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCatalogUnitFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackVatFaById', () => {
        it('Should return tracked VatFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVatFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
