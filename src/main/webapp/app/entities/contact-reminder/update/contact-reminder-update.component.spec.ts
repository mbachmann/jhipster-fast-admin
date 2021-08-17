jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactReminderService } from '../service/contact-reminder.service';
import { IContactReminder, ContactReminder } from '../contact-reminder.model';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

import { ContactReminderUpdateComponent } from './contact-reminder-update.component';

describe('Component Tests', () => {
  describe('ContactReminder Management Update Component', () => {
    let comp: ContactReminderUpdateComponent;
    let fixture: ComponentFixture<ContactReminderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactReminderService: ContactReminderService;
    let contactService: ContactService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactReminderUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactReminderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactReminderUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactReminderService = TestBed.inject(ContactReminderService);
      contactService = TestBed.inject(ContactService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Contact query and add missing value', () => {
        const contactReminder: IContactReminder = { id: 456 };
        const contact: IContact = { id: 65488 };
        contactReminder.contact = contact;

        const contactCollection: IContact[] = [{ id: 82371 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContacts = [contact];
        const expectedCollection: IContact[] = [...additionalContacts, ...contactCollection];
        jest.spyOn(contactService, 'addContactToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contactReminder });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContacts);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contactReminder: IContactReminder = { id: 456 };
        const contact: IContact = { id: 93918 };
        contactReminder.contact = contact;

        activatedRoute.data = of({ contactReminder });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contactReminder));
        expect(comp.contactsSharedCollection).toContain(contact);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactReminder>>();
        const contactReminder = { id: 123 };
        jest.spyOn(contactReminderService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactReminder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactReminder }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contactReminderService.update).toHaveBeenCalledWith(contactReminder);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactReminder>>();
        const contactReminder = new ContactReminder();
        jest.spyOn(contactReminderService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactReminder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactReminder }));
        saveSubject.complete();

        // THEN
        expect(contactReminderService.create).toHaveBeenCalledWith(contactReminder);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactReminder>>();
        const contactReminder = { id: 123 };
        jest.spyOn(contactReminderService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactReminder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contactReminderService.update).toHaveBeenCalledWith(contactReminder);
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
