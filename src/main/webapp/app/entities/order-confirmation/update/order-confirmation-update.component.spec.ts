jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrderConfirmationService } from '../service/order-confirmation.service';
import { IOrderConfirmation, OrderConfirmation } from '../order-confirmation.model';
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

import { OrderConfirmationUpdateComponent } from './order-confirmation-update.component';

describe('Component Tests', () => {
  describe('OrderConfirmation Management Update Component', () => {
    let comp: OrderConfirmationUpdateComponent;
    let fixture: ComponentFixture<OrderConfirmationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let orderConfirmationService: OrderConfirmationService;
    let contactService: ContactService;
    let contactAddressService: ContactAddressService;
    let contactPersonService: ContactPersonService;
    let layoutService: LayoutService;
    let signatureService: SignatureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrderConfirmationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrderConfirmationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrderConfirmationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      orderConfirmationService = TestBed.inject(OrderConfirmationService);
      contactService = TestBed.inject(ContactService);
      contactAddressService = TestBed.inject(ContactAddressService);
      contactPersonService = TestBed.inject(ContactPersonService);
      layoutService = TestBed.inject(LayoutService);
      signatureService = TestBed.inject(SignatureService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Contact query and add missing value', () => {
        const orderConfirmation: IOrderConfirmation = { id: 456 };
        const contact: IContact = { id: 27878 };
        orderConfirmation.contact = contact;

        const contactCollection: IContact[] = [{ id: 88493 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContacts = [contact];
        const expectedCollection: IContact[] = [...additionalContacts, ...contactCollection];
        jest.spyOn(contactService, 'addContactToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContacts);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactAddress query and add missing value', () => {
        const orderConfirmation: IOrderConfirmation = { id: 456 };
        const contactAddress: IContactAddress = { id: 77301 };
        orderConfirmation.contactAddress = contactAddress;
        const contactPrePageAddress: IContactAddress = { id: 53585 };
        orderConfirmation.contactPrePageAddress = contactPrePageAddress;

        const contactAddressCollection: IContactAddress[] = [{ id: 58787 }];
        jest.spyOn(contactAddressService, 'query').mockReturnValue(of(new HttpResponse({ body: contactAddressCollection })));
        const additionalContactAddresses = [contactAddress, contactPrePageAddress];
        const expectedCollection: IContactAddress[] = [...additionalContactAddresses, ...contactAddressCollection];
        jest.spyOn(contactAddressService, 'addContactAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(contactAddressService.query).toHaveBeenCalled();
        expect(contactAddressService.addContactAddressToCollectionIfMissing).toHaveBeenCalledWith(
          contactAddressCollection,
          ...additionalContactAddresses
        );
        expect(comp.contactAddressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactPerson query and add missing value', () => {
        const orderConfirmation: IOrderConfirmation = { id: 456 };
        const contactPerson: IContactPerson = { id: 4503 };
        orderConfirmation.contactPerson = contactPerson;

        const contactPersonCollection: IContactPerson[] = [{ id: 83263 }];
        jest.spyOn(contactPersonService, 'query').mockReturnValue(of(new HttpResponse({ body: contactPersonCollection })));
        const additionalContactPeople = [contactPerson];
        const expectedCollection: IContactPerson[] = [...additionalContactPeople, ...contactPersonCollection];
        jest.spyOn(contactPersonService, 'addContactPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(contactPersonService.query).toHaveBeenCalled();
        expect(contactPersonService.addContactPersonToCollectionIfMissing).toHaveBeenCalledWith(
          contactPersonCollection,
          ...additionalContactPeople
        );
        expect(comp.contactPeopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Layout query and add missing value', () => {
        const orderConfirmation: IOrderConfirmation = { id: 456 };
        const layout: ILayout = { id: 32764 };
        orderConfirmation.layout = layout;

        const layoutCollection: ILayout[] = [{ id: 72461 }];
        jest.spyOn(layoutService, 'query').mockReturnValue(of(new HttpResponse({ body: layoutCollection })));
        const additionalLayouts = [layout];
        const expectedCollection: ILayout[] = [...additionalLayouts, ...layoutCollection];
        jest.spyOn(layoutService, 'addLayoutToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(layoutService.query).toHaveBeenCalled();
        expect(layoutService.addLayoutToCollectionIfMissing).toHaveBeenCalledWith(layoutCollection, ...additionalLayouts);
        expect(comp.layoutsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Signature query and add missing value', () => {
        const orderConfirmation: IOrderConfirmation = { id: 456 };
        const layout: ISignature = { id: 80323 };
        orderConfirmation.layout = layout;

        const signatureCollection: ISignature[] = [{ id: 42880 }];
        jest.spyOn(signatureService, 'query').mockReturnValue(of(new HttpResponse({ body: signatureCollection })));
        const additionalSignatures = [layout];
        const expectedCollection: ISignature[] = [...additionalSignatures, ...signatureCollection];
        jest.spyOn(signatureService, 'addSignatureToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(signatureService.query).toHaveBeenCalled();
        expect(signatureService.addSignatureToCollectionIfMissing).toHaveBeenCalledWith(signatureCollection, ...additionalSignatures);
        expect(comp.signaturesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const orderConfirmation: IOrderConfirmation = { id: 456 };
        const contact: IContact = { id: 50522 };
        orderConfirmation.contact = contact;
        const contactAddress: IContactAddress = { id: 25228 };
        orderConfirmation.contactAddress = contactAddress;
        const contactPrePageAddress: IContactAddress = { id: 93673 };
        orderConfirmation.contactPrePageAddress = contactPrePageAddress;
        const contactPerson: IContactPerson = { id: 78852 };
        orderConfirmation.contactPerson = contactPerson;
        const layout: ILayout = { id: 88212 };
        orderConfirmation.layout = layout;
        const layout: ISignature = { id: 26103 };
        orderConfirmation.layout = layout;

        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(orderConfirmation));
        expect(comp.contactsSharedCollection).toContain(contact);
        expect(comp.contactAddressesSharedCollection).toContain(contactAddress);
        expect(comp.contactAddressesSharedCollection).toContain(contactPrePageAddress);
        expect(comp.contactPeopleSharedCollection).toContain(contactPerson);
        expect(comp.layoutsSharedCollection).toContain(layout);
        expect(comp.signaturesSharedCollection).toContain(layout);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderConfirmation>>();
        const orderConfirmation = { id: 123 };
        jest.spyOn(orderConfirmationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orderConfirmation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(orderConfirmationService.update).toHaveBeenCalledWith(orderConfirmation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderConfirmation>>();
        const orderConfirmation = new OrderConfirmation();
        jest.spyOn(orderConfirmationService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orderConfirmation }));
        saveSubject.complete();

        // THEN
        expect(orderConfirmationService.create).toHaveBeenCalledWith(orderConfirmation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderConfirmation>>();
        const orderConfirmation = { id: 123 };
        jest.spyOn(orderConfirmationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderConfirmation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(orderConfirmationService.update).toHaveBeenCalledWith(orderConfirmation);
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
    });
  });
});
