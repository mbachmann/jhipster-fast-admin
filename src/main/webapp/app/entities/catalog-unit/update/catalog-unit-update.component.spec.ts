jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CatalogUnitService } from '../service/catalog-unit.service';
import { ICatalogUnit, CatalogUnit } from '../catalog-unit.model';

import { CatalogUnitUpdateComponent } from './catalog-unit-update.component';

describe('Component Tests', () => {
  describe('CatalogUnit Management Update Component', () => {
    let comp: CatalogUnitUpdateComponent;
    let fixture: ComponentFixture<CatalogUnitUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let catalogUnitService: CatalogUnitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogUnitUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CatalogUnitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatalogUnitUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      catalogUnitService = TestBed.inject(CatalogUnitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const catalogUnit: ICatalogUnit = { id: 456 };

        activatedRoute.data = of({ catalogUnit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(catalogUnit));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CatalogUnit>>();
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
        const saveSubject = new Subject<HttpResponse<CatalogUnit>>();
        const catalogUnit = new CatalogUnit();
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
        const saveSubject = new Subject<HttpResponse<CatalogUnit>>();
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
