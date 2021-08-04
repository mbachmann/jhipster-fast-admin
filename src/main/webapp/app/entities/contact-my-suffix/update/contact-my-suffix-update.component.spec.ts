jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactMySuffixService } from '../service/contact-my-suffix.service';
import { IContactMySuffix, ContactMySuffix } from '../contact-my-suffix.model';
import { IContactAddressMySuffix } from 'app/entities/contact-address-my-suffix/contact-address-my-suffix.model';
import { ContactAddressMySuffixService } from 'app/entities/contact-address-my-suffix/service/contact-address-my-suffix.service';

import { ContactMySuffixUpdateComponent } from './contact-my-suffix-update.component';

describe('Component Tests', () => {
  describe('ContactMySuffix Management Update Component', () => {
    let comp: ContactMySuffixUpdateComponent;
    let fixture: ComponentFixture<ContactMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactService: ContactMySuffixService;
    let contactAddressService: ContactAddressMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactService = TestBed.inject(ContactMySuffixService);
      contactAddressService = TestBed.inject(ContactAddressMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call mainAddress query and add missing value', () => {
        const contact: IContactMySuffix = { id: 456 };
        const mainAddress: IContactAddressMySuffix = { id: 44710 };
        contact.mainAddress = mainAddress;

        const mainAddressCollection: IContactAddressMySuffix[] = [{ id: 92045 }];
        jest.spyOn(contactAddressService, 'query').mockReturnValue(of(new HttpResponse({ body: mainAddressCollection })));
        const expectedCollection: IContactAddressMySuffix[] = [mainAddress, ...mainAddressCollection];
        jest.spyOn(contactAddressService, 'addContactAddressMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contact });
        comp.ngOnInit();

        expect(contactAddressService.query).toHaveBeenCalled();
        expect(contactAddressService.addContactAddressMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
          mainAddressCollection,
          mainAddress
        );
        expect(comp.mainAddressesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contact: IContactMySuffix = { id: 456 };
        const mainAddress: IContactAddressMySuffix = { id: 68928 };
        contact.mainAddress = mainAddress;

        activatedRoute.data = of({ contact });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contact));
        expect(comp.mainAddressesCollection).toContain(mainAddress);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactMySuffix>>();
        const contact = { id: 123 };
        jest.spyOn(contactService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contact });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contact }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contactService.update).toHaveBeenCalledWith(contact);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactMySuffix>>();
        const contact = new ContactMySuffix();
        jest.spyOn(contactService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contact });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contact }));
        saveSubject.complete();

        // THEN
        expect(contactService.create).toHaveBeenCalledWith(contact);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactMySuffix>>();
        const contact = { id: 123 };
        jest.spyOn(contactService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contact });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contactService.update).toHaveBeenCalledWith(contact);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackContactAddressMySuffixById', () => {
        it('Should return tracked ContactAddressMySuffix primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactAddressMySuffixById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
