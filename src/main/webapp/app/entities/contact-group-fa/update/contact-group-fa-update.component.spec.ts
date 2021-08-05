jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactGroupFaService } from '../service/contact-group-fa.service';
import { IContactGroupFa, ContactGroupFa } from '../contact-group-fa.model';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';

import { ContactGroupFaUpdateComponent } from './contact-group-fa-update.component';

describe('Component Tests', () => {
  describe('ContactGroupFa Management Update Component', () => {
    let comp: ContactGroupFaUpdateComponent;
    let fixture: ComponentFixture<ContactGroupFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactGroupService: ContactGroupFaService;
    let contactService: ContactFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactGroupFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactGroupFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactGroupFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactGroupService = TestBed.inject(ContactGroupFaService);
      contactService = TestBed.inject(ContactFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContactFa query and add missing value', () => {
        const contactGroup: IContactGroupFa = { id: 456 };
        const contact: IContactFa = { id: 39894 };
        contactGroup.contact = contact;

        const contactCollection: IContactFa[] = [{ id: 22630 }];
        jest.spyOn(contactService, 'query').mockReturnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContactFas = [contact];
        const expectedCollection: IContactFa[] = [...additionalContactFas, ...contactCollection];
        jest.spyOn(contactService, 'addContactFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contactGroup });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactFaToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContactFas);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contactGroup: IContactGroupFa = { id: 456 };
        const contact: IContactFa = { id: 91292 };
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
        const saveSubject = new Subject<HttpResponse<ContactGroupFa>>();
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
        const saveSubject = new Subject<HttpResponse<ContactGroupFa>>();
        const contactGroup = new ContactGroupFa();
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
        const saveSubject = new Subject<HttpResponse<ContactGroupFa>>();
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
