jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ValueAddedTaxService } from '../service/value-added-tax.service';
import { IValueAddedTax, ValueAddedTax } from '../value-added-tax.model';

import { ValueAddedTaxUpdateComponent } from './value-added-tax-update.component';

describe('Component Tests', () => {
  describe('ValueAddedTax Management Update Component', () => {
    let comp: ValueAddedTaxUpdateComponent;
    let fixture: ComponentFixture<ValueAddedTaxUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let valueAddedTaxService: ValueAddedTaxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ValueAddedTaxUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ValueAddedTaxUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ValueAddedTaxUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      valueAddedTaxService = TestBed.inject(ValueAddedTaxService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const valueAddedTax: IValueAddedTax = { id: 456 };

        activatedRoute.data = of({ valueAddedTax });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(valueAddedTax));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ValueAddedTax>>();
        const valueAddedTax = { id: 123 };
        jest.spyOn(valueAddedTaxService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ valueAddedTax });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: valueAddedTax }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(valueAddedTaxService.update).toHaveBeenCalledWith(valueAddedTax);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ValueAddedTax>>();
        const valueAddedTax = new ValueAddedTax();
        jest.spyOn(valueAddedTaxService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ valueAddedTax });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: valueAddedTax }));
        saveSubject.complete();

        // THEN
        expect(valueAddedTaxService.create).toHaveBeenCalledWith(valueAddedTax);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ValueAddedTax>>();
        const valueAddedTax = { id: 123 };
        jest.spyOn(valueAddedTaxService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ valueAddedTax });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(valueAddedTaxService.update).toHaveBeenCalledWith(valueAddedTax);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
