jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactPersonService } from '../service/contact-person.service';
import { IContactPerson, ContactPerson } from '../contact-person.model';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

import { ContactPersonUpdateComponent } from './contact-person-update.component';

describe('Component Tests', () => {
  describe('ContactPerson Management Update Component', () => {
    let comp: ContactPersonUpdateComponent;
    let fixture: ComponentFixture<ContactPersonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactPersonService: ContactPersonService;
    let contactService: ContactService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactPersonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactPersonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactPersonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactPersonService = TestBed.inject(ContactPersonService);
      contactService = TestBed.inject(ContactService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Contact query and add missing value', () => {
        const contactPerson: IContactPerson = { id: 456 };
        const contact: IContact = { id: 12478 };
        contactPerson.contact = contact;

        const contactCollection: IContact[] = [{ id: 43852 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContacts = [contact];
        const expectedCollection: IContact[] = [...additionalContacts, ...contactCollection];
        jest.spyOn(contactService, 'addContactToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contactPerson });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContacts);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contactPerson: IContactPerson = { id: 456 };
        const contact: IContact = { id: 47381 };
        contactPerson.contact = contact;

        activatedRoute.data = of({ contactPerson });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contactPerson));
        expect(comp.contactsSharedCollection).toContain(contact);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactPerson>>();
        const contactPerson = { id: 123 };
        jest.spyOn(contactPersonService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactPerson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactPerson }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contactPersonService.update).toHaveBeenCalledWith(contactPerson);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactPerson>>();
        const contactPerson = new ContactPerson();
        jest.spyOn(contactPersonService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactPerson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactPerson }));
        saveSubject.complete();

        // THEN
        expect(contactPersonService.create).toHaveBeenCalledWith(contactPerson);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactPerson>>();
        const contactPerson = { id: 123 };
        jest.spyOn(contactPersonService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactPerson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contactPersonService.update).toHaveBeenCalledWith(contactPerson);
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
