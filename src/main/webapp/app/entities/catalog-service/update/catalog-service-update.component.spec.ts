jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CatalogServiceService } from '../service/catalog-service.service';
import { ICatalogService, CatalogService } from '../catalog-service.model';
import { ICatalogCategory } from 'app/entities/catalog-category/catalog-category.model';
import { CatalogCategoryService } from 'app/entities/catalog-category/service/catalog-category.service';
import { ICatalogUnit } from 'app/entities/catalog-unit/catalog-unit.model';
import { CatalogUnitService } from 'app/entities/catalog-unit/service/catalog-unit.service';
import { IValueAddedTax } from 'app/entities/value-added-tax/value-added-tax.model';
import { ValueAddedTaxService } from 'app/entities/value-added-tax/service/value-added-tax.service';

import { CatalogServiceUpdateComponent } from './catalog-service-update.component';

describe('Component Tests', () => {
  describe('CatalogService Management Update Component', () => {
    let comp: CatalogServiceUpdateComponent;
    let fixture: ComponentFixture<CatalogServiceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let catalogServiceService: CatalogServiceService;
    let catalogCategoryService: CatalogCategoryService;
    let catalogUnitService: CatalogUnitService;
    let valueAddedTaxService: ValueAddedTaxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogServiceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CatalogServiceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatalogServiceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      catalogServiceService = TestBed.inject(CatalogServiceService);
      catalogCategoryService = TestBed.inject(CatalogCategoryService);
      catalogUnitService = TestBed.inject(CatalogUnitService);
      valueAddedTaxService = TestBed.inject(ValueAddedTaxService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CatalogCategory query and add missing value', () => {
        const catalogService: ICatalogService = { id: 456 };
        const category: ICatalogCategory = { id: 25203 };
        catalogService.category = category;

        const catalogCategoryCollection: ICatalogCategory[] = [{ id: 66540 }];
        jest.spyOn(catalogCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogCategoryCollection })));
        const additionalCatalogCategories = [category];
        const expectedCollection: ICatalogCategory[] = [...additionalCatalogCategories, ...catalogCategoryCollection];
        jest.spyOn(catalogCategoryService, 'addCatalogCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ catalogService });
        comp.ngOnInit();

        expect(catalogCategoryService.query).toHaveBeenCalled();
        expect(catalogCategoryService.addCatalogCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          catalogCategoryCollection,
          ...additionalCatalogCategories
        );
        expect(comp.catalogCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CatalogUnit query and add missing value', () => {
        const catalogService: ICatalogService = { id: 456 };
        const unit: ICatalogUnit = { id: 27797 };
        catalogService.unit = unit;

        const catalogUnitCollection: ICatalogUnit[] = [{ id: 84941 }];
        jest.spyOn(catalogUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogUnitCollection })));
        const additionalCatalogUnits = [unit];
        const expectedCollection: ICatalogUnit[] = [...additionalCatalogUnits, ...catalogUnitCollection];
        jest.spyOn(catalogUnitService, 'addCatalogUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ catalogService });
        comp.ngOnInit();

        expect(catalogUnitService.query).toHaveBeenCalled();
        expect(catalogUnitService.addCatalogUnitToCollectionIfMissing).toHaveBeenCalledWith(
          catalogUnitCollection,
          ...additionalCatalogUnits
        );
        expect(comp.catalogUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ValueAddedTax query and add missing value', () => {
        const catalogService: ICatalogService = { id: 456 };
        const valueAddedTax: IValueAddedTax = { id: 90928 };
        catalogService.valueAddedTax = valueAddedTax;

        const valueAddedTaxCollection: IValueAddedTax[] = [{ id: 81449 }];
        jest.spyOn(valueAddedTaxService, 'query').mockReturnValue(of(new HttpResponse({ body: valueAddedTaxCollection })));
        const additionalValueAddedTaxes = [valueAddedTax];
        const expectedCollection: IValueAddedTax[] = [...additionalValueAddedTaxes, ...valueAddedTaxCollection];
        jest.spyOn(valueAddedTaxService, 'addValueAddedTaxToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ catalogService });
        comp.ngOnInit();

        expect(valueAddedTaxService.query).toHaveBeenCalled();
        expect(valueAddedTaxService.addValueAddedTaxToCollectionIfMissing).toHaveBeenCalledWith(
          valueAddedTaxCollection,
          ...additionalValueAddedTaxes
        );
        expect(comp.valueAddedTaxesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const catalogService: ICatalogService = { id: 456 };
        const category: ICatalogCategory = { id: 89106 };
        catalogService.category = category;
        const unit: ICatalogUnit = { id: 53669 };
        catalogService.unit = unit;
        const valueAddedTax: IValueAddedTax = { id: 19934 };
        catalogService.valueAddedTax = valueAddedTax;

        activatedRoute.data = of({ catalogService });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(catalogService));
        expect(comp.catalogCategoriesSharedCollection).toContain(category);
        expect(comp.catalogUnitsSharedCollection).toContain(unit);
        expect(comp.valueAddedTaxesSharedCollection).toContain(valueAddedTax);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogService>>();
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
        const saveSubject = new Subject<HttpResponse<CatalogService>>();
        const catalogService = new CatalogService();
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
        const saveSubject = new Subject<HttpResponse<CatalogService>>();
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
      describe('trackCatalogCategoryById', () => {
        it('Should return tracked CatalogCategory primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCatalogCategoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCatalogUnitById', () => {
        it('Should return tracked CatalogUnit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCatalogUnitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackValueAddedTaxById', () => {
        it('Should return tracked ValueAddedTax primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackValueAddedTaxById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
