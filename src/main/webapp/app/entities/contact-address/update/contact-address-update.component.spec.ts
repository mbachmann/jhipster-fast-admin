jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactAddressService } from '../service/contact-address.service';
import { IContactAddress, ContactAddress } from '../contact-address.model';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

import { ContactAddressUpdateComponent } from './contact-address-update.component';

describe('Component Tests', () => {
  describe('ContactAddress Management Update Component', () => {
    let comp: ContactAddressUpdateComponent;
    let fixture: ComponentFixture<ContactAddressUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactAddressService: ContactAddressService;
    let contactService: ContactService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactAddressUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactAddressUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactAddressUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactAddressService = TestBed.inject(ContactAddressService);
      contactService = TestBed.inject(ContactService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Contact query and add missing value', () => {
        const contactAddress: IContactAddress = { id: 456 };
        const contact: IContact = { id: 22440 };
        contactAddress.contact = contact;

        const contactCollection: IContact[] = [{ id: 60933 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContacts = [contact];
        const expectedCollection: IContact[] = [...additionalContacts, ...contactCollection];
        jest.spyOn(contactService, 'addContactToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contactAddress });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContacts);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contactAddress: IContactAddress = { id: 456 };
        const contact: IContact = { id: 6967 };
        contactAddress.contact = contact;

        activatedRoute.data = of({ contactAddress });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contactAddress));
        expect(comp.contactsSharedCollection).toContain(contact);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactAddress>>();
        const contactAddress = { id: 123 };
        jest.spyOn(contactAddressService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactAddress }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contactAddressService.update).toHaveBeenCalledWith(contactAddress);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactAddress>>();
        const contactAddress = new ContactAddress();
        jest.spyOn(contactAddressService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactAddress }));
        saveSubject.complete();

        // THEN
        expect(contactAddressService.create).toHaveBeenCalledWith(contactAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactAddress>>();
        const contactAddress = { id: 123 };
        jest.spyOn(contactAddressService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contactAddressService.update).toHaveBeenCalledWith(contactAddress);
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
    });
  });
});
