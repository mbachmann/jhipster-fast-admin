jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InvoiceFaService } from '../service/invoice-fa.service';
import { IInvoiceFa, InvoiceFa } from '../invoice-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';
import { IContactAddressFa } from 'app/entities/contact-address-fa/contact-address-fa.model';
import { ContactAddressFaService } from 'app/entities/contact-address-fa/service/contact-address-fa.service';
import { IContactPersonFa } from 'app/entities/contact-person-fa/contact-person-fa.model';
import { ContactPersonFaService } from 'app/entities/contact-person-fa/service/contact-person-fa.service';
import { ILayoutFa } from 'app/entities/layout-fa/layout-fa.model';
import { LayoutFaService } from 'app/entities/layout-fa/service/layout-fa.service';
import { ISignatureFa } from 'app/entities/signature-fa/signature-fa.model';
import { SignatureFaService } from 'app/entities/signature-fa/service/signature-fa.service';
import { IBankAccountFa } from 'app/entities/bank-account-fa/bank-account-fa.model';
import { BankAccountFaService } from 'app/entities/bank-account-fa/service/bank-account-fa.service';
import { IIsrFa } from 'app/entities/isr-fa/isr-fa.model';
import { IsrFaService } from 'app/entities/isr-fa/service/isr-fa.service';

import { InvoiceFaUpdateComponent } from './invoice-fa-update.component';

describe('Component Tests', () => {
  describe('InvoiceFa Management Update Component', () => {
    let comp: InvoiceFaUpdateComponent;
    let fixture: ComponentFixture<InvoiceFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let invoiceService: InvoiceFaService;
    let contactService: ContactFaService;
    let contactAddressService: ContactAddressFaService;
    let contactPersonService: ContactPersonFaService;
    let layoutService: LayoutFaService;
    let signatureService: SignatureFaService;
    let bankAccountService: BankAccountFaService;
    let isrService: IsrFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InvoiceFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InvoiceFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InvoiceFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      invoiceService = TestBed.inject(InvoiceFaService);
      contactService = TestBed.inject(ContactFaService);
      contactAddressService = TestBed.inject(ContactAddressFaService);
      contactPersonService = TestBed.inject(ContactPersonFaService);
      layoutService = TestBed.inject(LayoutFaService);
      signatureService = TestBed.inject(SignatureFaService);
      bankAccountService = TestBed.inject(BankAccountFaService);
      isrService = TestBed.inject(IsrFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContactFa query and add missing value', () => {
        const invoice: IInvoiceFa = { id: 456 };
        const contact: IContactFa = { id: 8156 };
        invoice.contact = contact;

        const contactCollection: IContactFa[] = [{ id: 1403 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContactFas = [contact];
        const expectedCollection: IContactFa[] = [...additionalContactFas, ...contactCollection];
        jest.spyOn(contactService, 'addContactFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactFaToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContactFas);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactAddressFa query and add missing value', () => {
        const invoice: IInvoiceFa = { id: 456 };
        const contactAddress: IContactAddressFa = { id: 23471 };
        invoice.contactAddress = contactAddress;
        const contactPrePageAddress: IContactAddressFa = { id: 28367 };
        invoice.contactPrePageAddress = contactPrePageAddress;

        const contactAddressCollection: IContactAddressFa[] = [{ id: 35810 }];
        jest.spyOn(contactAddressService, 'query').mockReturnValue(of(new HttpResponse({ body: contactAddressCollection })));
        const additionalContactAddressFas = [contactAddress, contactPrePageAddress];
        const expectedCollection: IContactAddressFa[] = [...additionalContactAddressFas, ...contactAddressCollection];
        jest.spyOn(contactAddressService, 'addContactAddressFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(contactAddressService.query).toHaveBeenCalled();
        expect(contactAddressService.addContactAddressFaToCollectionIfMissing).toHaveBeenCalledWith(
          contactAddressCollection,
          ...additionalContactAddressFas
        );
        expect(comp.contactAddressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactPersonFa query and add missing value', () => {
        const invoice: IInvoiceFa = { id: 456 };
        const contactPerson: IContactPersonFa = { id: 13960 };
        invoice.contactPerson = contactPerson;

        const contactPersonCollection: IContactPersonFa[] = [{ id: 39360 }];
        jest.spyOn(contactPersonService, 'query').mockReturnValue(of(new HttpResponse({ body: contactPersonCollection })));
        const additionalContactPersonFas = [contactPerson];
        const expectedCollection: IContactPersonFa[] = [...additionalContactPersonFas, ...contactPersonCollection];
        jest.spyOn(contactPersonService, 'addContactPersonFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(contactPersonService.query).toHaveBeenCalled();
        expect(contactPersonService.addContactPersonFaToCollectionIfMissing).toHaveBeenCalledWith(
          contactPersonCollection,
          ...additionalContactPersonFas
        );
        expect(comp.contactPeopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LayoutFa query and add missing value', () => {
        const invoice: IInvoiceFa = { id: 456 };
        const layout: ILayoutFa = { id: 49977 };
        invoice.layout = layout;

        const layoutCollection: ILayoutFa[] = [{ id: 96813 }];
        jest.spyOn(layoutService, 'query').mockReturnValue(of(new HttpResponse({ body: layoutCollection })));
        const additionalLayoutFas = [layout];
        const expectedCollection: ILayoutFa[] = [...additionalLayoutFas, ...layoutCollection];
        jest.spyOn(layoutService, 'addLayoutFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(layoutService.query).toHaveBeenCalled();
        expect(layoutService.addLayoutFaToCollectionIfMissing).toHaveBeenCalledWith(layoutCollection, ...additionalLayoutFas);
        expect(comp.layoutsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call SignatureFa query and add missing value', () => {
        const invoice: IInvoiceFa = { id: 456 };
        const layout: ISignatureFa = { id: 46554 };
        invoice.layout = layout;

        const signatureCollection: ISignatureFa[] = [{ id: 10067 }];
        jest.spyOn(signatureService, 'query').mockReturnValue(of(new HttpResponse({ body: signatureCollection })));
        const additionalSignatureFas = [layout];
        const expectedCollection: ISignatureFa[] = [...additionalSignatureFas, ...signatureCollection];
        jest.spyOn(signatureService, 'addSignatureFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(signatureService.query).toHaveBeenCalled();
        expect(signatureService.addSignatureFaToCollectionIfMissing).toHaveBeenCalledWith(signatureCollection, ...additionalSignatureFas);
        expect(comp.signaturesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call BankAccountFa query and add missing value', () => {
        const invoice: IInvoiceFa = { id: 456 };
        const bankAccount: IBankAccountFa = { id: 63053 };
        invoice.bankAccount = bankAccount;

        const bankAccountCollection: IBankAccountFa[] = [{ id: 6600 }];
        jest.spyOn(bankAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: bankAccountCollection })));
        const additionalBankAccountFas = [bankAccount];
        const expectedCollection: IBankAccountFa[] = [...additionalBankAccountFas, ...bankAccountCollection];
        jest.spyOn(bankAccountService, 'addBankAccountFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(bankAccountService.query).toHaveBeenCalled();
        expect(bankAccountService.addBankAccountFaToCollectionIfMissing).toHaveBeenCalledWith(
          bankAccountCollection,
          ...additionalBankAccountFas
        );
        expect(comp.bankAccountsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call IsrFa query and add missing value', () => {
        const invoice: IInvoiceFa = { id: 456 };
        const isr: IIsrFa = { id: 58008 };
        invoice.isr = isr;

        const isrCollection: IIsrFa[] = [{ id: 3518 }];
        jest.spyOn(isrService, 'query').mockReturnValue(of(new HttpResponse({ body: isrCollection })));
        const additionalIsrFas = [isr];
        const expectedCollection: IIsrFa[] = [...additionalIsrFas, ...isrCollection];
        jest.spyOn(isrService, 'addIsrFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(isrService.query).toHaveBeenCalled();
        expect(isrService.addIsrFaToCollectionIfMissing).toHaveBeenCalledWith(isrCollection, ...additionalIsrFas);
        expect(comp.isrsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const invoice: IInvoiceFa = { id: 456 };
        const contact: IContactFa = { id: 3169 };
        invoice.contact = contact;
        const contactAddress: IContactAddressFa = { id: 41579 };
        invoice.contactAddress = contactAddress;
        const contactPrePageAddress: IContactAddressFa = { id: 94097 };
        invoice.contactPrePageAddress = contactPrePageAddress;
        const contactPerson: IContactPersonFa = { id: 57117 };
        invoice.contactPerson = contactPerson;
        const layout: ILayoutFa = { id: 86331 };
        invoice.layout = layout;
        const layout: ISignatureFa = { id: 70757 };
        invoice.layout = layout;
        const bankAccount: IBankAccountFa = { id: 52570 };
        invoice.bankAccount = bankAccount;
        const isr: IIsrFa = { id: 73345 };
        invoice.isr = isr;

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(invoice));
        expect(comp.contactsSharedCollection).toContain(contact);
        expect(comp.contactAddressesSharedCollection).toContain(contactAddress);
        expect(comp.contactAddressesSharedCollection).toContain(contactPrePageAddress);
        expect(comp.contactPeopleSharedCollection).toContain(contactPerson);
        expect(comp.layoutsSharedCollection).toContain(layout);
        expect(comp.signaturesSharedCollection).toContain(layout);
        expect(comp.bankAccountsSharedCollection).toContain(bankAccount);
        expect(comp.isrsSharedCollection).toContain(isr);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<InvoiceFa>>();
        const invoice = { id: 123 };
        jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: invoice }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(invoiceService.update).toHaveBeenCalledWith(invoice);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<InvoiceFa>>();
        const invoice = new InvoiceFa();
        jest.spyOn(invoiceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: invoice }));
        saveSubject.complete();

        // THEN
        expect(invoiceService.create).toHaveBeenCalledWith(invoice);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<InvoiceFa>>();
        const invoice = { id: 123 };
        jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(invoiceService.update).toHaveBeenCalledWith(invoice);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackContactFaById', () => {
        it('Should return tracked ContactFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactAddressFaById', () => {
        it('Should return tracked ContactAddressFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactAddressFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactPersonFaById', () => {
        it('Should return tracked ContactPersonFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactPersonFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLayoutFaById', () => {
        it('Should return tracked LayoutFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLayoutFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSignatureFaById', () => {
        it('Should return tracked SignatureFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSignatureFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackBankAccountFaById', () => {
        it('Should return tracked BankAccountFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBankAccountFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackIsrFaById', () => {
        it('Should return tracked IsrFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackIsrFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
