jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactAccountService } from '../service/contact-account.service';
import { IContactAccount, ContactAccount } from '../contact-account.model';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

import { ContactAccountUpdateComponent } from './contact-account-update.component';

describe('Component Tests', () => {
  describe('ContactAccount Management Update Component', () => {
    let comp: ContactAccountUpdateComponent;
    let fixture: ComponentFixture<ContactAccountUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactAccountService: ContactAccountService;
    let contactService: ContactService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactAccountUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactAccountUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactAccountService = TestBed.inject(ContactAccountService);
      contactService = TestBed.inject(ContactService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Contact query and add missing value', () => {
        const contactAccount: IContactAccount = { id: 456 };
        const contact: IContact = { id: 87057 };
        contactAccount.contact = contact;

        const contactCollection: IContact[] = [{ id: 5271 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContacts = [contact];
        const expectedCollection: IContact[] = [...additionalContacts, ...contactCollection];
        jest.spyOn(contactService, 'addContactToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contactAccount });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContacts);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contactAccount: IContactAccount = { id: 456 };
        const contact: IContact = { id: 82222 };
        contactAccount.contact = contact;

        activatedRoute.data = of({ contactAccount });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contactAccount));
        expect(comp.contactsSharedCollection).toContain(contact);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactAccount>>();
        const contactAccount = { id: 123 };
        jest.spyOn(contactAccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactAccount }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contactAccountService.update).toHaveBeenCalledWith(contactAccount);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactAccount>>();
        const contactAccount = new ContactAccount();
        jest.spyOn(contactAccountService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactAccount }));
        saveSubject.complete();

        // THEN
        expect(contactAccountService.create).toHaveBeenCalledWith(contactAccount);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactAccount>>();
        const contactAccount = { id: 123 };
        jest.spyOn(contactAccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contactAccountService.update).toHaveBeenCalledWith(contactAccount);
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
