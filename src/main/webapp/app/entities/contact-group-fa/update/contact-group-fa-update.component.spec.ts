jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactGroupFaService } from '../service/contact-group-fa.service';
import { IContactGroupFa, ContactGroupFa } from '../contact-group-fa.model';

import { ContactGroupFaUpdateComponent } from './contact-group-fa-update.component';

describe('Component Tests', () => {
  describe('ContactGroupFa Management Update Component', () => {
    let comp: ContactGroupFaUpdateComponent;
    let fixture: ComponentFixture<ContactGroupFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactGroupService: ContactGroupFaService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const contactGroup: IContactGroupFa = { id: 456 };

        activatedRoute.data = of({ contactGroup });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contactGroup));
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
  });
});
