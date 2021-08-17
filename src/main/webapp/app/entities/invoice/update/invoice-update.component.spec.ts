jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InvoiceService } from '../service/invoice.service';
import { IInvoice, Invoice } from '../invoice.model';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';
import { IContactAddress } from 'app/entities/contact-address/contact-address.model';
import { ContactAddressService } from 'app/entities/contact-address/service/contact-address.service';
import { IContactPerson } from 'app/entities/contact-person/contact-person.model';
import { ContactPersonService } from 'app/entities/contact-person/service/contact-person.service';
import { ILayout } from 'app/entities/layout/layout.model';
import { LayoutService } from 'app/entities/layout/service/layout.service';
import { ISignature } from 'app/entities/signature/signature.model';
import { SignatureService } from 'app/entities/signature/service/signature.service';
import { IBankAccount } from 'app/entities/bank-account/bank-account.model';
import { BankAccountService } from 'app/entities/bank-account/service/bank-account.service';
import { IIsr } from 'app/entities/isr/isr.model';
import { IsrService } from 'app/entities/isr/service/isr.service';

import { InvoiceUpdateComponent } from './invoice-update.component';

describe('Component Tests', () => {
  describe('Invoice Management Update Component', () => {
    let comp: InvoiceUpdateComponent;
    let fixture: ComponentFixture<InvoiceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let invoiceService: InvoiceService;
    let contactService: ContactService;
    let contactAddressService: ContactAddressService;
    let contactPersonService: ContactPersonService;
    let layoutService: LayoutService;
    let signatureService: SignatureService;
    let bankAccountService: BankAccountService;
    let isrService: IsrService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InvoiceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InvoiceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InvoiceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      invoiceService = TestBed.inject(InvoiceService);
      contactService = TestBed.inject(ContactService);
      contactAddressService = TestBed.inject(ContactAddressService);
      contactPersonService = TestBed.inject(ContactPersonService);
      layoutService = TestBed.inject(LayoutService);
      signatureService = TestBed.inject(SignatureService);
      bankAccountService = TestBed.inject(BankAccountService);
      isrService = TestBed.inject(IsrService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Contact query and add missing value', () => {
        const invoice: IInvoice = { id: 456 };
        const contact: IContact = { id: 8156 };
        invoice.contact = contact;

        const contactCollection: IContact[] = [{ id: 1403 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContacts = [contact];
        const expectedCollection: IContact[] = [...additionalContacts, ...contactCollection];
        jest.spyOn(contactService, 'addContactToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContacts);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactAddress query and add missing value', () => {
        const invoice: IInvoice = { id: 456 };
        const contactAddress: IContactAddress = { id: 23471 };
        invoice.contactAddress = contactAddress;
        const contactPrePageAddress: IContactAddress = { id: 28367 };
        invoice.contactPrePageAddress = contactPrePageAddress;

        const contactAddressCollection: IContactAddress[] = [{ id: 35810 }];
        jest.spyOn(contactAddressService, 'query').mockReturnValue(of(new HttpResponse({ body: contactAddressCollection })));
        const additionalContactAddresses = [contactAddress, contactPrePageAddress];
        const expectedCollection: IContactAddress[] = [...additionalContactAddresses, ...contactAddressCollection];
        jest.spyOn(contactAddressService, 'addContactAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(contactAddressService.query).toHaveBeenCalled();
        expect(contactAddressService.addContactAddressToCollectionIfMissing).toHaveBeenCalledWith(
          contactAddressCollection,
          ...additionalContactAddresses
        );
        expect(comp.contactAddressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactPerson query and add missing value', () => {
        const invoice: IInvoice = { id: 456 };
        const contactPerson: IContactPerson = { id: 13960 };
        invoice.contactPerson = contactPerson;

        const contactPersonCollection: IContactPerson[] = [{ id: 39360 }];
        jest.spyOn(contactPersonService, 'query').mockReturnValue(of(new HttpResponse({ body: contactPersonCollection })));
        const additionalContactPeople = [contactPerson];
        const expectedCollection: IContactPerson[] = [...additionalContactPeople, ...contactPersonCollection];
        jest.spyOn(contactPersonService, 'addContactPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(contactPersonService.query).toHaveBeenCalled();
        expect(contactPersonService.addContactPersonToCollectionIfMissing).toHaveBeenCalledWith(
          contactPersonCollection,
          ...additionalContactPeople
        );
        expect(comp.contactPeopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Layout query and add missing value', () => {
        const invoice: IInvoice = { id: 456 };
        const layout: ILayout = { id: 49977 };
        invoice.layout = layout;

        const layoutCollection: ILayout[] = [{ id: 96813 }];
        jest.spyOn(layoutService, 'query').mockReturnValue(of(new HttpResponse({ body: layoutCollection })));
        const additionalLayouts = [layout];
        const expectedCollection: ILayout[] = [...additionalLayouts, ...layoutCollection];
        jest.spyOn(layoutService, 'addLayoutToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(layoutService.query).toHaveBeenCalled();
        expect(layoutService.addLayoutToCollectionIfMissing).toHaveBeenCalledWith(layoutCollection, ...additionalLayouts);
        expect(comp.layoutsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Signature query and add missing value', () => {
        const invoice: IInvoice = { id: 456 };
        const layout: ISignature = { id: 46554 };
        invoice.layout = layout;

        const signatureCollection: ISignature[] = [{ id: 10067 }];
        jest.spyOn(signatureService, 'query').mockReturnValue(of(new HttpResponse({ body: signatureCollection })));
        const additionalSignatures = [layout];
        const expectedCollection: ISignature[] = [...additionalSignatures, ...signatureCollection];
        jest.spyOn(signatureService, 'addSignatureToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(signatureService.query).toHaveBeenCalled();
        expect(signatureService.addSignatureToCollectionIfMissing).toHaveBeenCalledWith(signatureCollection, ...additionalSignatures);
        expect(comp.signaturesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call BankAccount query and add missing value', () => {
        const invoice: IInvoice = { id: 456 };
        const bankAccount: IBankAccount = { id: 63053 };
        invoice.bankAccount = bankAccount;

        const bankAccountCollection: IBankAccount[] = [{ id: 6600 }];
        jest.spyOn(bankAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: bankAccountCollection })));
        const additionalBankAccounts = [bankAccount];
        const expectedCollection: IBankAccount[] = [...additionalBankAccounts, ...bankAccountCollection];
        jest.spyOn(bankAccountService, 'addBankAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(bankAccountService.query).toHaveBeenCalled();
        expect(bankAccountService.addBankAccountToCollectionIfMissing).toHaveBeenCalledWith(
          bankAccountCollection,
          ...additionalBankAccounts
        );
        expect(comp.bankAccountsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Isr query and add missing value', () => {
        const invoice: IInvoice = { id: 456 };
        const isr: IIsr = { id: 58008 };
        invoice.isr = isr;

        const isrCollection: IIsr[] = [{ id: 3518 }];
        jest.spyOn(isrService, 'query').mockReturnValue(of(new HttpResponse({ body: isrCollection })));
        const additionalIsrs = [isr];
        const expectedCollection: IIsr[] = [...additionalIsrs, ...isrCollection];
        jest.spyOn(isrService, 'addIsrToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoice });
        comp.ngOnInit();

        expect(isrService.query).toHaveBeenCalled();
        expect(isrService.addIsrToCollectionIfMissing).toHaveBeenCalledWith(isrCollection, ...additionalIsrs);
        expect(comp.isrsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const invoice: IInvoice = { id: 456 };
        const contact: IContact = { id: 3169 };
        invoice.contact = contact;
        const contactAddress: IContactAddress = { id: 41579 };
        invoice.contactAddress = contactAddress;
        const contactPrePageAddress: IContactAddress = { id: 94097 };
        invoice.contactPrePageAddress = contactPrePageAddress;
        const contactPerson: IContactPerson = { id: 57117 };
        invoice.contactPerson = contactPerson;
        const layout: ILayout = { id: 86331 };
        invoice.layout = layout;
        const layout: ISignature = { id: 70757 };
        invoice.layout = layout;
        const bankAccount: IBankAccount = { id: 52570 };
        invoice.bankAccount = bankAccount;
        const isr: IIsr = { id: 73345 };
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
        const saveSubject = new Subject<HttpResponse<Invoice>>();
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
        const saveSubject = new Subject<HttpResponse<Invoice>>();
        const invoice = new Invoice();
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
        const saveSubject = new Subject<HttpResponse<Invoice>>();
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
      describe('trackContactById', () => {
        it('Should return tracked Contact primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactAddressById', () => {
        it('Should return tracked ContactAddress primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactAddressById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactPersonById', () => {
        it('Should return tracked ContactPerson primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactPersonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLayoutById', () => {
        it('Should return tracked Layout primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLayoutById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSignatureById', () => {
        it('Should return tracked Signature primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSignatureById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackBankAccountById', () => {
        it('Should return tracked BankAccount primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBankAccountById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackIsrById', () => {
        it('Should return tracked Isr primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackIsrById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
