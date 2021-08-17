jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VatFaService } from '../service/vat-fa.service';
import { IVatFa, VatFa } from '../vat-fa.model';

import { VatFaUpdateComponent } from './vat-fa-update.component';

describe('Component Tests', () => {
  describe('VatFa Management Update Component', () => {
    let comp: VatFaUpdateComponent;
    let fixture: ComponentFixture<VatFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vatService: VatFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VatFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VatFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VatFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      vatService = TestBed.inject(VatFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const vat: IVatFa = { id: 456 };

        activatedRoute.data = of({ vat });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vat));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<VatFa>>();
        const vat = { id: 123 };
        jest.spyOn(vatService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vat }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(vatService.update).toHaveBeenCalledWith(vat);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<VatFa>>();
        const vat = new VatFa();
        jest.spyOn(vatService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vat }));
        saveSubject.complete();

        // THEN
        expect(vatService.create).toHaveBeenCalledWith(vat);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<VatFa>>();
        const vat = { id: 123 };
        jest.spyOn(vatService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(vatService.update).toHaveBeenCalledWith(vat);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
