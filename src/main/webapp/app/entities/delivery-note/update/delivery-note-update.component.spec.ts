jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DeliveryNoteService } from '../service/delivery-note.service';
import { IDeliveryNote, DeliveryNote } from '../delivery-note.model';
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

import { DeliveryNoteUpdateComponent } from './delivery-note-update.component';

describe('Component Tests', () => {
  describe('DeliveryNote Management Update Component', () => {
    let comp: DeliveryNoteUpdateComponent;
    let fixture: ComponentFixture<DeliveryNoteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let deliveryNoteService: DeliveryNoteService;
    let contactService: ContactService;
    let contactAddressService: ContactAddressService;
    let contactPersonService: ContactPersonService;
    let layoutService: LayoutService;
    let signatureService: SignatureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DeliveryNoteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DeliveryNoteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeliveryNoteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      deliveryNoteService = TestBed.inject(DeliveryNoteService);
      contactService = TestBed.inject(ContactService);
      contactAddressService = TestBed.inject(ContactAddressService);
      contactPersonService = TestBed.inject(ContactPersonService);
      layoutService = TestBed.inject(LayoutService);
      signatureService = TestBed.inject(SignatureService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Contact query and add missing value', () => {
        const deliveryNote: IDeliveryNote = { id: 456 };
        const contact: IContact = { id: 33805 };
        deliveryNote.contact = contact;

        const contactCollection: IContact[] = [{ id: 80917 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContacts = [contact];
        const expectedCollection: IContact[] = [...additionalContacts, ...contactCollection];
        jest.spyOn(contactService, 'addContactToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ deliveryNote });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContacts);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactAddress query and add missing value', () => {
        const deliveryNote: IDeliveryNote = { id: 456 };
        const contactAddress: IContactAddress = { id: 53980 };
        deliveryNote.contactAddress = contactAddress;
        const contactPrePageAddress: IContactAddress = { id: 50302 };
        deliveryNote.contactPrePageAddress = contactPrePageAddress;

        const contactAddressCollection: IContactAddress[] = [{ id: 56722 }];
        jest.spyOn(contactAddressService, 'query').mockReturnValue(of(new HttpResponse({ body: contactAddressCollection })));
        const additionalContactAddresses = [contactAddress, contactPrePageAddress];
        const expectedCollection: IContactAddress[] = [...additionalContactAddresses, ...contactAddressCollection];
        jest.spyOn(contactAddressService, 'addContactAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ deliveryNote });
        comp.ngOnInit();

        expect(contactAddressService.query).toHaveBeenCalled();
        expect(contactAddressService.addContactAddressToCollectionIfMissing).toHaveBeenCalledWith(
          contactAddressCollection,
          ...additionalContactAddresses
        );
        expect(comp.contactAddressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactPerson query and add missing value', () => {
        const deliveryNote: IDeliveryNote = { id: 456 };
        const contactPerson: IContactPerson = { id: 10757 };
        deliveryNote.contactPerson = contactPerson;

        const contactPersonCollection: IContactPerson[] = [{ id: 29869 }];
        jest.spyOn(contactPersonService, 'query').mockReturnValue(of(new HttpResponse({ body: contactPersonCollection })));
        const additionalContactPeople = [contactPerson];
        const expectedCollection: IContactPerson[] = [...additionalContactPeople, ...contactPersonCollection];
        jest.spyOn(contactPersonService, 'addContactPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ deliveryNote });
        comp.ngOnInit();

        expect(contactPersonService.query).toHaveBeenCalled();
        expect(contactPersonService.addContactPersonToCollectionIfMissing).toHaveBeenCalledWith(
          contactPersonCollection,
          ...additionalContactPeople
        );
        expect(comp.contactPeopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Layout query and add missing value', () => {
        const deliveryNote: IDeliveryNote = { id: 456 };
        const layout: ILayout = { id: 39461 };
        deliveryNote.layout = layout;

        const layoutCollection: ILayout[] = [{ id: 58453 }];
        jest.spyOn(layoutService, 'query').mockReturnValue(of(new HttpResponse({ body: layoutCollection })));
        const additionalLayouts = [layout];
        const expectedCollection: ILayout[] = [...additionalLayouts, ...layoutCollection];
        jest.spyOn(layoutService, 'addLayoutToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ deliveryNote });
        comp.ngOnInit();

        expect(layoutService.query).toHaveBeenCalled();
        expect(layoutService.addLayoutToCollectionIfMissing).toHaveBeenCalledWith(layoutCollection, ...additionalLayouts);
        expect(comp.layoutsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Signature query and add missing value', () => {
        const deliveryNote: IDeliveryNote = { id: 456 };
        const signature: ISignature = { id: 36299 };
        deliveryNote.signature = signature;

        const signatureCollection: ISignature[] = [{ id: 86599 }];
        jest.spyOn(signatureService, 'query').mockReturnValue(of(new HttpResponse({ body: signatureCollection })));
        const additionalSignatures = [signature];
        const expectedCollection: ISignature[] = [...additionalSignatures, ...signatureCollection];
        jest.spyOn(signatureService, 'addSignatureToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ deliveryNote });
        comp.ngOnInit();

        expect(signatureService.query).toHaveBeenCalled();
        expect(signatureService.addSignatureToCollectionIfMissing).toHaveBeenCalledWith(signatureCollection, ...additionalSignatures);
        expect(comp.signaturesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const deliveryNote: IDeliveryNote = { id: 456 };
        const contact: IContact = { id: 99111 };
        deliveryNote.contact = contact;
        const contactAddress: IContactAddress = { id: 86775 };
        deliveryNote.contactAddress = contactAddress;
        const contactPrePageAddress: IContactAddress = { id: 3980 };
        deliveryNote.contactPrePageAddress = contactPrePageAddress;
        const contactPerson: IContactPerson = { id: 96388 };
        deliveryNote.contactPerson = contactPerson;
        const layout: ILayout = { id: 84129 };
        deliveryNote.layout = layout;
        const signature: ISignature = { id: 41354 };
        deliveryNote.signature = signature;

        activatedRoute.data = of({ deliveryNote });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(deliveryNote));
        expect(comp.contactsSharedCollection).toContain(contact);
        expect(comp.contactAddressesSharedCollection).toContain(contactAddress);
        expect(comp.contactAddressesSharedCollection).toContain(contactPrePageAddress);
        expect(comp.contactPeopleSharedCollection).toContain(contactPerson);
        expect(comp.layoutsSharedCollection).toContain(layout);
        expect(comp.signaturesSharedCollection).toContain(signature);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DeliveryNote>>();
        const deliveryNote = { id: 123 };
        jest.spyOn(deliveryNoteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ deliveryNote });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: deliveryNote }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(deliveryNoteService.update).toHaveBeenCalledWith(deliveryNote);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DeliveryNote>>();
        const deliveryNote = new DeliveryNote();
        jest.spyOn(deliveryNoteService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ deliveryNote });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: deliveryNote }));
        saveSubject.complete();

        // THEN
        expect(deliveryNoteService.create).toHaveBeenCalledWith(deliveryNote);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DeliveryNote>>();
        const deliveryNote = { id: 123 };
        jest.spyOn(deliveryNoteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ deliveryNote });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(deliveryNoteService.update).toHaveBeenCalledWith(deliveryNote);
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
