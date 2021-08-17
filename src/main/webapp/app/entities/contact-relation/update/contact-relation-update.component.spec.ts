jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactRelationService } from '../service/contact-relation.service';
import { IContactRelation, ContactRelation } from '../contact-relation.model';

import { ContactRelationUpdateComponent } from './contact-relation-update.component';

describe('Component Tests', () => {
  describe('ContactRelation Management Update Component', () => {
    let comp: ContactRelationUpdateComponent;
    let fixture: ComponentFixture<ContactRelationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactRelationService: ContactRelationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactRelationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactRelationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactRelationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactRelationService = TestBed.inject(ContactRelationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const contactRelation: IContactRelation = { id: 456 };

        activatedRoute.data = of({ contactRelation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contactRelation));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactRelation>>();
        const contactRelation = { id: 123 };
        jest.spyOn(contactRelationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactRelation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactRelation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contactRelationService.update).toHaveBeenCalledWith(contactRelation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactRelation>>();
        const contactRelation = new ContactRelation();
        jest.spyOn(contactRelationService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactRelation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactRelation }));
        saveSubject.complete();

        // THEN
        expect(contactRelationService.create).toHaveBeenCalledWith(contactRelation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactRelation>>();
        const contactRelation = { id: 123 };
        jest.spyOn(contactRelationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactRelation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contactRelationService.update).toHaveBeenCalledWith(contactRelation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
