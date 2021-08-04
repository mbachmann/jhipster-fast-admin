jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactGroupMySuffixService } from '../service/contact-group-my-suffix.service';
import { IContactGroupMySuffix, ContactGroupMySuffix } from '../contact-group-my-suffix.model';
import { IContactMySuffix } from 'app/entities/contact-my-suffix/contact-my-suffix.model';
import { ContactMySuffixService } from 'app/entities/contact-my-suffix/service/contact-my-suffix.service';

import { ContactGroupMySuffixUpdateComponent } from './contact-group-my-suffix-update.component';

describe('Component Tests', () => {
  describe('ContactGroupMySuffix Management Update Component', () => {
    let comp: ContactGroupMySuffixUpdateComponent;
    let fixture: ComponentFixture<ContactGroupMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactGroupService: ContactGroupMySuffixService;
    let contactService: ContactMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactGroupMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactGroupMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactGroupMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactGroupService = TestBed.inject(ContactGroupMySuffixService);
      contactService = TestBed.inject(ContactMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContactMySuffix query and add missing value', () => {
        const contactGroup: IContactGroupMySuffix = { id: 456 };
        const contact: IContactMySuffix = { id: 39894 };
        contactGroup.contact = contact;

        const contactCollection: IContactMySuffix[] = [{ id: 22630 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContactMySuffixes = [contact];
        const expectedCollection: IContactMySuffix[] = [...additionalContactMySuffixes, ...contactCollection];
        jest.spyOn(contactService, 'addContactMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contactGroup });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
          contactCollection,
          ...additionalContactMySuffixes
        );
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contactGroup: IContactGroupMySuffix = { id: 456 };
        const contact: IContactMySuffix = { id: 91292 };
        contactGroup.contact = contact;

        activatedRoute.data = of({ contactGroup });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contactGroup));
        expect(comp.contactsSharedCollection).toContain(contact);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactGroupMySuffix>>();
        const contactGroup = { id: 123 };
        jest.spyOn(contactGroupService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactGroup }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contactGroupService.update).toHaveBeenCalledWith(contactGroup);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactGroupMySuffix>>();
        const contactGroup = new ContactGroupMySuffix();
        jest.spyOn(contactGroupService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactGroup }));
        saveSubject.complete();

        // THEN
        expect(contactGroupService.create).toHaveBeenCalledWith(contactGroup);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactGroupMySuffix>>();
        const contactGroup = { id: 123 };
        jest.spyOn(contactGroupService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contactGroupService.update).toHaveBeenCalledWith(contactGroup);
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
    });
  });
});
