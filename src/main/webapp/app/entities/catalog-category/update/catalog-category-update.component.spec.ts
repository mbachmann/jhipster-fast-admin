jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CatalogCategoryService } from '../service/catalog-category.service';
import { ICatalogCategory, CatalogCategory } from '../catalog-category.model';

import { CatalogCategoryUpdateComponent } from './catalog-category-update.component';

describe('Component Tests', () => {
  describe('CatalogCategory Management Update Component', () => {
    let comp: CatalogCategoryUpdateComponent;
    let fixture: ComponentFixture<CatalogCategoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let catalogCategoryService: CatalogCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogCategoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CatalogCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatalogCategoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      catalogCategoryService = TestBed.inject(CatalogCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const catalogCategory: ICatalogCategory = { id: 456 };

        activatedRoute.data = of({ catalogCategory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(catalogCategory));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogCategory>>();
        const catalogCategory = { id: 123 };
        jest.spyOn(catalogCategoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalogCategory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(catalogCategoryService.update).toHaveBeenCalledWith(catalogCategory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogCategory>>();
        const catalogCategory = new CatalogCategory();
        jest.spyOn(catalogCategoryService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalogCategory }));
        saveSubject.complete();

        // THEN
        expect(catalogCategoryService.create).toHaveBeenCalledWith(catalogCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogCategory>>();
        const catalogCategory = { id: 123 };
        jest.spyOn(catalogCategoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(catalogCategoryService.update).toHaveBeenCalledWith(catalogCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
