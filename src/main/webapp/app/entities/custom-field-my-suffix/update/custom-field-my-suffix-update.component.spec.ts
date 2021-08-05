jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CustomFieldMySuffixService } from '../service/custom-field-my-suffix.service';
import { ICustomFieldMySuffix, CustomFieldMySuffix } from '../custom-field-my-suffix.model';
import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';
import { ContactMySuffixService } from 'app/entities/contact-my-suffix/service/contact-my-suffix.service';
import { IContactPersonMySuffix } from 'app/entities/contact-person-my-suffix/contact-person-my-suffix.model';
import { ContactPersonMySuffixService } from 'app/entities/contact-person-my-suffix/service/contact-person-my-suffix.service';

import { CustomFieldMySuffixUpdateComponent } from './custom-field-my-suffix-update.component';

describe('Component Tests', () => {
  describe('CustomFieldMySuffix Management Update Component', () => {
    let comp: CustomFieldMySuffixUpdateComponent;
    let fixture: ComponentFixture<CustomFieldMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let customFieldService: CustomFieldMySuffixService;
    let contactService: ContactMySuffixService;
    let contactPersonService: ContactPersonMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CustomFieldMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CustomFieldMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomFieldMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      customFieldService = TestBed.inject(CustomFieldMySuffixService);
      contactService = TestBed.inject(ContactMySuffixService);
      contactPersonService = TestBed.inject(ContactPersonMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContactMySuffix query and add missing value', () => {
        const customField: ICustomFieldMySuffix = { id: 456 };
        const contact: IContactMySuffix = { id: 17808 };
        customField.contact = contact;

        const contactCollection: IContactMySuffix[] = [{ id: 14974 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContactMySuffixes = [contact];
        const expectedCollection: IContactMySuffix[] = [...additionalContactMySuffixes, ...contactCollection];
        jest.spyOn(contactService, 'addContactMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
          contactCollection,
          ...additionalContactMySuffixes
        );
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactPersonMySuffix query and add missing value', () => {
        const customField: ICustomFieldMySuffix = { id: 456 };
        const contactPerson: IContactPersonMySuffix = { id: 53965 };
        customField.contactPerson = contactPerson;

        const contactPersonCollection: IContactPersonMySuffix[] = [{ id: 72674 }];
        jest.spyOn(contactPersonService, 'query').mockReturnValue(of(new HttpResponse({ body: contactPersonCollection })));
        const additionalContactPersonMySuffixes = [contactPerson];
        const expectedCollection: IContactPersonMySuffix[] = [...additionalContactPersonMySuffixes, ...contactPersonCollection];
        jest.spyOn(contactPersonService, 'addContactPersonMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ customField });
        comp.ngOnInit();

        expect(contactPersonService.query).toHaveBeenCalled();
        expect(contactPersonService.addContactPersonMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
          contactPersonCollection,
          ...additionalContactPersonMySuffixes
        );
        expect(comp.contactPeopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const customField: ICustomFieldMySuffix = { id: 456 };
        const contact: IContactMySuffix = { id: 67059 };
        customField.contact = contact;
        const contactPerson: IContactPersonMySuffix = { id: 29544 };
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
        const saveSubject = new Subject<HttpResponse<CustomFieldMySuffix>>();
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
        const saveSubject = new Subject<HttpResponse<CustomFieldMySuffix>>();
        const customField = new CustomFieldMySuffix();
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
        const saveSubject = new Subject<HttpResponse<CustomFieldMySuffix>>();
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
      describe('trackContactMySuffixById', () => {
        it('Should return tracked ContactMySuffix primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactMySuffixById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactPersonMySuffixById', () => {
        it('Should return tracked ContactPersonMySuffix primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactPersonMySuffixById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
