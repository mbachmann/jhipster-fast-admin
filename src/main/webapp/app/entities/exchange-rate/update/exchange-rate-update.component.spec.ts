jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExchangeRateService } from '../service/exchange-rate.service';
import { IExchangeRate, ExchangeRate } from '../exchange-rate.model';

import { ExchangeRateUpdateComponent } from './exchange-rate-update.component';

describe('Component Tests', () => {
  describe('ExchangeRate Management Update Component', () => {
    let comp: ExchangeRateUpdateComponent;
    let fixture: ComponentFixture<ExchangeRateUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let exchangeRateService: ExchangeRateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExchangeRateUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExchangeRateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExchangeRateUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      exchangeRateService = TestBed.inject(ExchangeRateService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const exchangeRate: IExchangeRate = { id: 456 };

        activatedRoute.data = of({ exchangeRate });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(exchangeRate));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExchangeRate>>();
        const exchangeRate = { id: 123 };
        jest.spyOn(exchangeRateService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ exchangeRate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exchangeRate }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(exchangeRateService.update).toHaveBeenCalledWith(exchangeRate);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExchangeRate>>();
        const exchangeRate = new ExchangeRate();
        jest.spyOn(exchangeRateService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ exchangeRate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exchangeRate }));
        saveSubject.complete();

        // THEN
        expect(exchangeRateService.create).toHaveBeenCalledWith(exchangeRate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExchangeRate>>();
        const exchangeRate = { id: 123 };
        jest.spyOn(exchangeRateService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ exchangeRate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(exchangeRateService.update).toHaveBeenCalledWith(exchangeRate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
