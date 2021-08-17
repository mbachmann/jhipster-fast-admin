jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactAccountFaService } from '../service/contact-account-fa.service';
import { IContactAccountFa, ContactAccountFa } from '../contact-account-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';

import { ContactAccountFaUpdateComponent } from './contact-account-fa-update.component';

describe('Component Tests', () => {
  describe('ContactAccountFa Management Update Component', () => {
    let comp: ContactAccountFaUpdateComponent;
    let fixture: ComponentFixture<ContactAccountFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactAccountService: ContactAccountFaService;
    let contactService: ContactFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactAccountFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactAccountFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactAccountFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactAccountService = TestBed.inject(ContactAccountFaService);
      contactService = TestBed.inject(ContactFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContactFa query and add missing value', () => {
        const contactAccount: IContactAccountFa = { id: 456 };
        const contact: IContactFa = { id: 87057 };
        contactAccount.contact = contact;

        const contactCollection: IContactFa[] = [{ id: 5271 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContactFas = [contact];
        const expectedCollection: IContactFa[] = [...additionalContactFas, ...contactCollection];
        jest.spyOn(contactService, 'addContactFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contactAccount });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactFaToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContactFas);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contactAccount: IContactAccountFa = { id: 456 };
        const contact: IContactFa = { id: 82222 };
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
        const saveSubject = new Subject<HttpResponse<ContactAccountFa>>();
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
        const saveSubject = new Subject<HttpResponse<ContactAccountFa>>();
        const contactAccount = new ContactAccountFa();
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
        const saveSubject = new Subject<HttpResponse<ContactAccountFa>>();
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
