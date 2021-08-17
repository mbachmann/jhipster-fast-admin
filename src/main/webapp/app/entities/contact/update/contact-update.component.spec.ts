jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactService } from '../service/contact.service';
import { IContact, Contact } from '../contact.model';
import { IContactRelation } from 'app/entities/contact-relation/contact-relation.model';
import { ContactRelationService } from 'app/entities/contact-relation/service/contact-relation.service';
import { IContactGroup } from 'app/entities/contact-group/contact-group.model';
import { ContactGroupService } from 'app/entities/contact-group/service/contact-group.service';

import { ContactUpdateComponent } from './contact-update.component';

describe('Component Tests', () => {
  describe('Contact Management Update Component', () => {
    let comp: ContactUpdateComponent;
    let fixture: ComponentFixture<ContactUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactService: ContactService;
    let contactRelationService: ContactRelationService;
    let contactGroupService: ContactGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactService = TestBed.inject(ContactService);
      contactRelationService = TestBed.inject(ContactRelationService);
      contactGroupService = TestBed.inject(ContactGroupService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContactRelation query and add missing value', () => {
        const contact: IContact = { id: 456 };
        const relations: IContactRelation[] = [{ id: 7767 }];
        contact.relations = relations;

        const contactRelationCollection: IContactRelation[] = [{ id: 47446 }];
        jest.spyOn(contactRelationService, 'query').mockReturnValue(of(new HttpResponse({ body: contactRelationCollection })));
        const additionalContactRelations = [...relations];
        const expectedCollection: IContactRelation[] = [...additionalContactRelations, ...contactRelationCollection];
        jest.spyOn(contactRelationService, 'addContactRelationToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contact });
        comp.ngOnInit();

        expect(contactRelationService.query).toHaveBeenCalled();
        expect(contactRelationService.addContactRelationToCollectionIfMissing).toHaveBeenCalledWith(
          contactRelationCollection,
          ...additionalContactRelations
        );
        expect(comp.contactRelationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ContactGroup query and add missing value', () => {
        const contact: IContact = { id: 456 };
        const groups: IContactGroup[] = [{ id: 14793 }];
        contact.groups = groups;

        const contactGroupCollection: IContactGroup[] = [{ id: 16297 }];
        jest.spyOn(contactGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: contactGroupCollection })));
        const additionalContactGroups = [...groups];
        const expectedCollection: IContactGroup[] = [...additionalContactGroups, ...contactGroupCollection];
        jest.spyOn(contactGroupService, 'addContactGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contact });
        comp.ngOnInit();

        expect(contactGroupService.query).toHaveBeenCalled();
        expect(contactGroupService.addContactGroupToCollectionIfMissing).toHaveBeenCalledWith(
          contactGroupCollection,
          ...additionalContactGroups
        );
        expect(comp.contactGroupsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contact: IContact = { id: 456 };
        const relations: IContactRelation = { id: 28140 };
        contact.relations = [relations];
        const groups: IContactGroup = { id: 18449 };
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
        const saveSubject = new Subject<HttpResponse<Contact>>();
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
        const saveSubject = new Subject<HttpResponse<Contact>>();
        const contact = new Contact();
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
        const saveSubject = new Subject<HttpResponse<Contact>>();
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
      describe('trackContactRelationById', () => {
        it('Should return tracked ContactRelation primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactRelationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactGroupById', () => {
        it('Should return tracked ContactGroup primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactGroupById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedContactRelation', () => {
        it('Should return option if no ContactRelation is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedContactRelation(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected ContactRelation for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedContactRelation(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this ContactRelation is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedContactRelation(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedContactGroup', () => {
        it('Should return option if no ContactGroup is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedContactGroup(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected ContactGroup for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedContactGroup(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this ContactGroup is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedContactGroup(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
