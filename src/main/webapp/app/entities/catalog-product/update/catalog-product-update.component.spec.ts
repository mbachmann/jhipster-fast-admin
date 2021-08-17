jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CatalogProductService } from '../service/catalog-product.service';
import { ICatalogProduct, CatalogProduct } from '../catalog-product.model';
import { ICatalogCategory } from 'app/entities/catalog-category/catalog-category.model';
import { CatalogCategoryService } from 'app/entities/catalog-category/service/catalog-category.service';
import { ICatalogUnit } from 'app/entities/catalog-unit/catalog-unit.model';
import { CatalogUnitService } from 'app/entities/catalog-unit/service/catalog-unit.service';
import { IValueAddedTax } from 'app/entities/value-added-tax/value-added-tax.model';
import { ValueAddedTaxService } from 'app/entities/value-added-tax/service/value-added-tax.service';

import { CatalogProductUpdateComponent } from './catalog-product-update.component';

describe('Component Tests', () => {
  describe('CatalogProduct Management Update Component', () => {
    let comp: CatalogProductUpdateComponent;
    let fixture: ComponentFixture<CatalogProductUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let catalogProductService: CatalogProductService;
    let catalogCategoryService: CatalogCategoryService;
    let catalogUnitService: CatalogUnitService;
    let valueAddedTaxService: ValueAddedTaxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogProductUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CatalogProductUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatalogProductUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      catalogProductService = TestBed.inject(CatalogProductService);
      catalogCategoryService = TestBed.inject(CatalogCategoryService);
      catalogUnitService = TestBed.inject(CatalogUnitService);
      valueAddedTaxService = TestBed.inject(ValueAddedTaxService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CatalogCategory query and add missing value', () => {
        const catalogProduct: ICatalogProduct = { id: 456 };
        const category: ICatalogCategory = { id: 23145 };
        catalogProduct.category = category;

        const catalogCategoryCollection: ICatalogCategory[] = [{ id: 25277 }];
        jest.spyOn(catalogCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogCategoryCollection })));
        const additionalCatalogCategories = [category];
        const expectedCollection: ICatalogCategory[] = [...additionalCatalogCategories, ...catalogCategoryCollection];
        jest.spyOn(catalogCategoryService, 'addCatalogCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ catalogProduct });
        comp.ngOnInit();

        expect(catalogCategoryService.query).toHaveBeenCalled();
        expect(catalogCategoryService.addCatalogCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          catalogCategoryCollection,
          ...additionalCatalogCategories
        );
        expect(comp.catalogCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CatalogUnit query and add missing value', () => {
        const catalogProduct: ICatalogProduct = { id: 456 };
        const unit: ICatalogUnit = { id: 10387 };
        catalogProduct.unit = unit;

        const catalogUnitCollection: ICatalogUnit[] = [{ id: 18865 }];
        jest.spyOn(catalogUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogUnitCollection })));
        const additionalCatalogUnits = [unit];
        const expectedCollection: ICatalogUnit[] = [...additionalCatalogUnits, ...catalogUnitCollection];
        jest.spyOn(catalogUnitService, 'addCatalogUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ catalogProduct });
        comp.ngOnInit();

        expect(catalogUnitService.query).toHaveBeenCalled();
        expect(catalogUnitService.addCatalogUnitToCollectionIfMissing).toHaveBeenCalledWith(
          catalogUnitCollection,
          ...additionalCatalogUnits
        );
        expect(comp.catalogUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ValueAddedTax query and add missing value', () => {
        const catalogProduct: ICatalogProduct = { id: 456 };
        const valueAddedTax: IValueAddedTax = { id: 61695 };
        catalogProduct.valueAddedTax = valueAddedTax;

        const valueAddedTaxCollection: IValueAddedTax[] = [{ id: 16367 }];
        jest.spyOn(valueAddedTaxService, 'query').mockReturnValue(of(new HttpResponse({ body: valueAddedTaxCollection })));
        const additionalValueAddedTaxes = [valueAddedTax];
        const expectedCollection: IValueAddedTax[] = [...additionalValueAddedTaxes, ...valueAddedTaxCollection];
        jest.spyOn(valueAddedTaxService, 'addValueAddedTaxToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ catalogProduct });
        comp.ngOnInit();

        expect(valueAddedTaxService.query).toHaveBeenCalled();
        expect(valueAddedTaxService.addValueAddedTaxToCollectionIfMissing).toHaveBeenCalledWith(
          valueAddedTaxCollection,
          ...additionalValueAddedTaxes
        );
        expect(comp.valueAddedTaxesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const catalogProduct: ICatalogProduct = { id: 456 };
        const category: ICatalogCategory = { id: 47010 };
        catalogProduct.category = category;
        const unit: ICatalogUnit = { id: 90744 };
        catalogProduct.unit = unit;
        const valueAddedTax: IValueAddedTax = { id: 10910 };
        catalogProduct.valueAddedTax = valueAddedTax;

        activatedRoute.data = of({ catalogProduct });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(catalogProduct));
        expect(comp.catalogCategoriesSharedCollection).toContain(category);
        expect(comp.catalogUnitsSharedCollection).toContain(unit);
        expect(comp.valueAddedTaxesSharedCollection).toContain(valueAddedTax);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogProduct>>();
        const catalogProduct = { id: 123 };
        jest.spyOn(catalogProductService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogProduct });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalogProduct }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(catalogProductService.update).toHaveBeenCalledWith(catalogProduct);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogProduct>>();
        const catalogProduct = new CatalogProduct();
        jest.spyOn(catalogProductService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogProduct });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalogProduct }));
        saveSubject.complete();

        // THEN
        expect(catalogProductService.create).toHaveBeenCalledWith(catalogProduct);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogProduct>>();
        const catalogProduct = { id: 123 };
        jest.spyOn(catalogProductService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogProduct });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(catalogProductService.update).toHaveBeenCalledWith(catalogProduct);
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
