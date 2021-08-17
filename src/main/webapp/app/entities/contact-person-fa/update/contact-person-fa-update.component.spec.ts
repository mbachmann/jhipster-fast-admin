jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactPersonFaService } from '../service/contact-person-fa.service';
import { IContactPersonFa, ContactPersonFa } from '../contact-person-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';

import { ContactPersonFaUpdateComponent } from './contact-person-fa-update.component';

describe('Component Tests', () => {
  describe('ContactPersonFa Management Update Component', () => {
    let comp: ContactPersonFaUpdateComponent;
    let fixture: ComponentFixture<ContactPersonFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactPersonService: ContactPersonFaService;
    let contactService: ContactFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactPersonFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactPersonFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactPersonFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactPersonService = TestBed.inject(ContactPersonFaService);
      contactService = TestBed.inject(ContactFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContactFa query and add missing value', () => {
        const contactPerson: IContactPersonFa = { id: 456 };
        const contact: IContactFa = { id: 12478 };
        contactPerson.contact = contact;

        const contactCollection: IContactFa[] = [{ id: 43852 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContactFas = [contact];
        const expectedCollection: IContactFa[] = [...additionalContactFas, ...contactCollection];
        jest.spyOn(contactService, 'addContactFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contactPerson });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactFaToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContactFas);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contactPerson: IContactPersonFa = { id: 456 };
        const contact: IContactFa = { id: 47381 };
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
        const saveSubject = new Subject<HttpResponse<ContactPersonFa>>();
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
        const saveSubject = new Subject<HttpResponse<ContactPersonFa>>();
        const contactPerson = new ContactPersonFa();
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
        const saveSubject = new Subject<HttpResponse<ContactPersonFa>>();
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
