jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactFaService } from '../service/contact-fa.service';
import { IContactFa, ContactFa } from '../contact-fa.model';
import { IContactRelationFa } from 'app/entities/contact-relation-fa/contact-relation-fa.model';
import { ContactRelationFaService } from 'app/entities/contact-relation-fa/service/contact-relation-fa.service';
import { IContactGroupFa } from 'app/entities/contact-group-fa/contact-group-fa.model';
import { ContactGroupFaService } from 'app/entities/contact-group-fa/service/contact-group-fa.service';

import { ContactFaUpdateComponent } from './contact-fa-update.component';

describe('Component Tests', () => {
  describe('ContactFa Management Update Component', () => {
    let comp: ContactFaUpdateComponent;
    let fixture: ComponentFixture<ContactFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactService: ContactFaService;
    let contactRelationService: ContactRelationFaService;
    let contactGroupService: ContactGroupFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactService = TestBed.inject(ContactFaService);
      contactRelationService = TestBed.inject(ContactRelationFaService);
      contactGroupService = TestBed.inject(ContactGroupFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContactRelationFa query and add missing value', () => {
        const contact: IContactFa = { id: 456 };
        const relations: IContactRelationFa[] = [{ id: 7767 }];
        contact.relations = relations;

        const contactRelationCollection: IContactRelationFa[] = [{ id: 47446 }];
        jest.spyOn(contactRelationService, 'query').mockReturnValue(of(new HttpResponse({ body: contactRelationCollection })));
        const additionalContactRelationFas = [...relations];
        const expectedCollection: IContactRelationFa[] = [...additionalContactRelationFas, ...contactRelationCollection];
        jest.spyOn(contactRelationService, 'addContactRelationFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contact });
        comp.ngOnInit();

        expect(contactRelationService.query).toHaveBeenCalled();
        expect(contactRelationService.addContactRelationFaToCollectionIfMissing).toHaveBeenCalledWith(
          contactRelationCollection,
          ...additionalContactRelationFas
        );
        expect(comp.contactRelationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactGroupFa query and add missing value', () => {
        const contact: IContactFa = { id: 456 };
        const groups: IContactGroupFa[] = [{ id: 14793 }];
        contact.groups = groups;

        const contactGroupCollection: IContactGroupFa[] = [{ id: 16297 }];
        jest.spyOn(contactGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: contactGroupCollection })));
        const additionalContactGroupFas = [...groups];
        const expectedCollection: IContactGroupFa[] = [...additionalContactGroupFas, ...contactGroupCollection];
        jest.spyOn(contactGroupService, 'addContactGroupFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contact });
        comp.ngOnInit();

        expect(contactGroupService.query).toHaveBeenCalled();
        expect(contactGroupService.addContactGroupFaToCollectionIfMissing).toHaveBeenCalledWith(
          contactGroupCollection,
          ...additionalContactGroupFas
        );
        expect(comp.contactGroupsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contact: IContactFa = { id: 456 };
        const relations: IContactRelationFa = { id: 28140 };
        contact.relations = [relations];
        const groups: IContactGroupFa = { id: 18449 };
        contact.groups = [groups];

        activatedRoute.data = of({ contact });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contact));
        expect(comp.contactRelationsSharedCollection).toContain(relations);
        expect(comp.contactGroupsSharedCollection).toContain(groups);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactFa>>();
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
        const saveSubject = new Subject<HttpResponse<ContactFa>>();
        const contact = new ContactFa();
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
        const saveSubject = new Subject<HttpResponse<ContactFa>>();
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
      describe('trackContactRelationFaById', () => {
        it('Should return tracked ContactRelationFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactRelationFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactGroupFaById', () => {
        it('Should return tracked ContactGroupFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactGroupFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedContactRelationFa', () => {
        it('Should return option if no ContactRelationFa is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedContactRelationFa(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected ContactRelationFa for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedContactRelationFa(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this ContactRelationFa is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedContactRelationFa(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedContactGroupFa', () => {
        it('Should return option if no ContactGroupFa is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedContactGroupFa(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected ContactGroupFa for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedContactGroupFa(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this ContactGroupFa is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedContactGroupFa(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
