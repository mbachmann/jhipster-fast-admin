jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CatalogCategoryFaService } from '../service/catalog-category-fa.service';
import { ICatalogCategoryFa, CatalogCategoryFa } from '../catalog-category-fa.model';

import { CatalogCategoryFaUpdateComponent } from './catalog-category-fa-update.component';

describe('Component Tests', () => {
  describe('CatalogCategoryFa Management Update Component', () => {
    let comp: CatalogCategoryFaUpdateComponent;
    let fixture: ComponentFixture<CatalogCategoryFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let catalogCategoryService: CatalogCategoryFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogCategoryFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CatalogCategoryFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatalogCategoryFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      catalogCategoryService = TestBed.inject(CatalogCategoryFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const catalogCategory: ICatalogCategoryFa = { id: 456 };

        activatedRoute.data = of({ catalogCategory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(catalogCategory));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogCategoryFa>>();
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
        const saveSubject = new Subject<HttpResponse<CatalogCategoryFa>>();
        const catalogCategory = new CatalogCategoryFa();
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
        const saveSubject = new Subject<HttpResponse<CatalogCategoryFa>>();
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
