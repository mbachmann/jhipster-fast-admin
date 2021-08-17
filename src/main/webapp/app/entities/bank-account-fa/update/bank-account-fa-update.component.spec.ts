jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BankAccountFaService } from '../service/bank-account-fa.service';
import { IBankAccountFa, BankAccountFa } from '../bank-account-fa.model';

import { BankAccountFaUpdateComponent } from './bank-account-fa-update.component';

describe('Component Tests', () => {
  describe('BankAccountFa Management Update Component', () => {
    let comp: BankAccountFaUpdateComponent;
    let fixture: ComponentFixture<BankAccountFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let bankAccountService: BankAccountFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BankAccountFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BankAccountFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BankAccountFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      bankAccountService = TestBed.inject(BankAccountFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const bankAccount: IBankAccountFa = { id: 456 };

        activatedRoute.data = of({ bankAccount });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(bankAccount));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BankAccountFa>>();
        const bankAccount = { id: 123 };
        jest.spyOn(bankAccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ bankAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bankAccount }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(bankAccountService.update).toHaveBeenCalledWith(bankAccount);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BankAccountFa>>();
        const bankAccount = new BankAccountFa();
        jest.spyOn(bankAccountService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ bankAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bankAccount }));
        saveSubject.complete();

        // THEN
        expect(bankAccountService.create).toHaveBeenCalledWith(bankAccount);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BankAccountFa>>();
        const bankAccount = { id: 123 };
        jest.spyOn(bankAccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ bankAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(bankAccountService.update).toHaveBeenCalledWith(bankAccount);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
