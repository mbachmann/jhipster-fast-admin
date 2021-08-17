jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CatalogUnitFaService } from '../service/catalog-unit-fa.service';
import { ICatalogUnitFa, CatalogUnitFa } from '../catalog-unit-fa.model';

import { CatalogUnitFaUpdateComponent } from './catalog-unit-fa-update.component';

describe('Component Tests', () => {
  describe('CatalogUnitFa Management Update Component', () => {
    let comp: CatalogUnitFaUpdateComponent;
    let fixture: ComponentFixture<CatalogUnitFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let catalogUnitService: CatalogUnitFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogUnitFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CatalogUnitFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatalogUnitFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      catalogUnitService = TestBed.inject(CatalogUnitFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const catalogUnit: ICatalogUnitFa = { id: 456 };

        activatedRoute.data = of({ catalogUnit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(catalogUnit));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogUnitFa>>();
        const catalogUnit = { id: 123 };
        jest.spyOn(catalogUnitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalogUnit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(catalogUnitService.update).toHaveBeenCalledWith(catalogUnit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogUnitFa>>();
        const catalogUnit = new CatalogUnitFa();
        jest.spyOn(catalogUnitService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalogUnit }));
        saveSubject.complete();

        // THEN
        expect(catalogUnitService.create).toHaveBeenCalledWith(catalogUnit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogUnitFa>>();
        const catalogUnit = { id: 123 };
        jest.spyOn(catalogUnitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalogUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(catalogUnitService.update).toHaveBeenCalledWith(catalogUnit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
