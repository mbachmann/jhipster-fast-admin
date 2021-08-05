jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactPersonMySuffixService } from '../service/contact-person-my-suffix.service';
import { IContactPersonMySuffix, ContactPersonMySuffix } from '../contact-person-my-suffix.model';

import { ContactPersonMySuffixUpdateComponent } from './contact-person-my-suffix-update.component';

describe('Component Tests', () => {
  describe('ContactPersonMySuffix Management Update Component', () => {
    let comp: ContactPersonMySuffixUpdateComponent;
    let fixture: ComponentFixture<ContactPersonMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactPersonService: ContactPersonMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactPersonMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactPersonMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactPersonMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactPersonService = TestBed.inject(ContactPersonMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const contactPerson: IContactPersonMySuffix = { id: 456 };

        activatedRoute.data = of({ contactPerson });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contactPerson));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactPersonMySuffix>>();
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
        const saveSubject = new Subject<HttpResponse<ContactPersonMySuffix>>();
        const contactPerson = new ContactPersonMySuffix();
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
        const saveSubject = new Subject<HttpResponse<ContactPersonMySuffix>>();
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
  });
});
