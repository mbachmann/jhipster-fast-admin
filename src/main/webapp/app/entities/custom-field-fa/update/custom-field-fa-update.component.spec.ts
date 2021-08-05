jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CustomFieldFaService } from '../service/custom-field-fa.service';
import { ICustomFieldFa, CustomFieldFa } from '../custom-field-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';
import { IContactPersonFa } from 'app/entities/contact-person-fa/contact-person-fa.model';
import { ContactPersonFaService } from 'app/entities/contact-person-fa/service/contact-person-fa.service';

import { CustomFieldFaUpdateComponent } from './custom-field-fa-update.component';

describe('Component Tests', () => {
  describe('CustomFieldFa Management Update Component', () => {
    let comp: CustomFieldFaUpdateComponent;
    let fixture: ComponentFixture<CustomFieldFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let customFieldService: CustomFieldFaService;
    let contactService: ContactFaService;
    let contactPersonService: ContactPersonFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CustomFieldFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CustomFieldFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomFieldFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      customFieldService = TestBed.inject(CustomFieldFaService);
      contactService = TestBed.inject(ContactFaService);
      contactPersonService = TestBed.inject(ContactPersonFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContactFa query and add missing value', () => {
        const customField: ICustomFieldFa = { id: 456 };
        const contact: IContactFa = { id: 17808 };
        customField.contact = contact;

        const contactCollection: IContactFa[] = [{ id: 14974 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContactFas = [contact];
        const expectedCollection: IContactFa[] = [...additionalContactFas, ...contactCollection];
        jest.spyOn(contactService, 'addContactFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactFaToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContactFas);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactPersonFa query and add missing value', () => {
        const customField: ICustomFieldFa = { id: 456 };
        const contactPerson: IContactPersonFa = { id: 53965 };
        customField.contactPerson = contactPerson;

        const contactPersonCollection: IContactPersonFa[] = [{ id: 72674 }];
        jest.spyOn(contactPersonService, 'query').mockReturnValue(of(new HttpResponse({ body: contactPersonCollection })));
        const additionalContactPersonFas = [contactPerson];
        const expectedCollection: IContactPersonFa[] = [...additionalContactPersonFas, ...contactPersonCollection];
        jest.spyOn(contactPersonService, 'addContactPersonFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        expect(contactPersonService.query).toHaveBeenCalled();
        expect(contactPersonService.addContactPersonFaToCollectionIfMissing).toHaveBeenCalledWith(
          contactPersonCollection,
          ...additionalContactPersonFas
        );
        expect(comp.contactPeopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const customField: ICustomFieldFa = { id: 456 };
        const contact: IContactFa = { id: 67059 };
        customField.contact = contact;
        const contactPerson: IContactPersonFa = { id: 29544 };
        customField.contactPerson = contactPerson;

        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(customField));
        expect(comp.contactsSharedCollection).toContain(contact);
        expect(comp.contactPeopleSharedCollection).toContain(contactPerson);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CustomFieldFa>>();
        const customField = { id: 123 };
        jest.spyOn(customFieldService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: customField }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(customFieldService.update).toHaveBeenCalledWith(customField);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CustomFieldFa>>();
        const customField = new CustomFieldFa();
        jest.spyOn(customFieldService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: customField }));
        saveSubject.complete();

        // THEN
        expect(customFieldService.create).toHaveBeenCalledWith(customField);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CustomFieldFa>>();
        const customField = { id: 123 };
        jest.spyOn(customFieldService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(customFieldService.update).toHaveBeenCalledWith(customField);
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

      describe('trackContactPersonFaById', () => {
        it('Should return tracked ContactPersonFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactPersonFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
