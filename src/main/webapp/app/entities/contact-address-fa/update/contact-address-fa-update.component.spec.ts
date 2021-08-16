jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactAddressFaService } from '../service/contact-address-fa.service';
import { IContactAddressFa, ContactAddressFa } from '../contact-address-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';

import { ContactAddressFaUpdateComponent } from './contact-address-fa-update.component';

describe('Component Tests', () => {
  describe('ContactAddressFa Management Update Component', () => {
    let comp: ContactAddressFaUpdateComponent;
    let fixture: ComponentFixture<ContactAddressFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactAddressService: ContactAddressFaService;
    let contactService: ContactFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactAddressFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactAddressFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactAddressFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactAddressService = TestBed.inject(ContactAddressFaService);
      contactService = TestBed.inject(ContactFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContactFa query and add missing value', () => {
        const contactAddress: IContactAddressFa = { id: 456 };
        const contact: IContactFa = { id: 22440 };
        contactAddress.contact = contact;

        const contactCollection: IContactFa[] = [{ id: 60933 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContactFas = [contact];
        const expectedCollection: IContactFa[] = [...additionalContactFas, ...contactCollection];
        jest.spyOn(contactService, 'addContactFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contactAddress });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactFaToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContactFas);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contactAddress: IContactAddressFa = { id: 456 };
        const contact: IContactFa = { id: 6967 };
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
        const saveSubject = new Subject<HttpResponse<ContactAddressFa>>();
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
        const saveSubject = new Subject<HttpResponse<ContactAddressFa>>();
        const contactAddress = new ContactAddressFa();
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
        const saveSubject = new Subject<HttpResponse<ContactAddressFa>>();
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
      describe('trackContactFaById', () => {
        it('Should return tracked ContactFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
