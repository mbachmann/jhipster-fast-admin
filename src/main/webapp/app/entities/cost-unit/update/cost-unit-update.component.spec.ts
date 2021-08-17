jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CostUnitService } from '../service/cost-unit.service';
import { ICostUnit, CostUnit } from '../cost-unit.model';

import { CostUnitUpdateComponent } from './cost-unit-update.component';

describe('Component Tests', () => {
  describe('CostUnit Management Update Component', () => {
    let comp: CostUnitUpdateComponent;
    let fixture: ComponentFixture<CostUnitUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let costUnitService: CostUnitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CostUnitUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CostUnitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CostUnitUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      costUnitService = TestBed.inject(CostUnitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const costUnit: ICostUnit = { id: 456 };

        activatedRoute.data = of({ costUnit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(costUnit));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CostUnit>>();
        const costUnit = { id: 123 };
        jest.spyOn(costUnitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ costUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: costUnit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(costUnitService.update).toHaveBeenCalledWith(costUnit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CostUnit>>();
        const costUnit = new CostUnit();
        jest.spyOn(costUnitService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ costUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: costUnit }));
        saveSubject.complete();

        // THEN
        expect(costUnitService.create).toHaveBeenCalledWith(costUnit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CostUnit>>();
        const costUnit = { id: 123 };
        jest.spyOn(costUnitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ costUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(costUnitService.update).toHaveBeenCalledWith(costUnit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
