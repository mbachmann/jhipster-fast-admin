jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactReminderFaService } from '../service/contact-reminder-fa.service';
import { IContactReminderFa, ContactReminderFa } from '../contact-reminder-fa.model';

import { ContactReminderFaUpdateComponent } from './contact-reminder-fa-update.component';

describe('Component Tests', () => {
  describe('ContactReminderFa Management Update Component', () => {
    let comp: ContactReminderFaUpdateComponent;
    let fixture: ComponentFixture<ContactReminderFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactReminderService: ContactReminderFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactReminderFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactReminderFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactReminderFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactReminderService = TestBed.inject(ContactReminderFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const contactReminder: IContactReminderFa = { id: 456 };

        activatedRoute.data = of({ contactReminder });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contactReminder));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactReminderFa>>();
        const contactReminder = { id: 123 };
        jest.spyOn(contactReminderService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactReminder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactReminder }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contactReminderService.update).toHaveBeenCalledWith(contactReminder);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactReminderFa>>();
        const contactReminder = new ContactReminderFa();
        jest.spyOn(contactReminderService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactReminder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contactReminder }));
        saveSubject.complete();

        // THEN
        expect(contactReminderService.create).toHaveBeenCalledWith(contactReminder);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactReminderFa>>();
        const contactReminder = { id: 123 };
        jest.spyOn(contactReminderService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contactReminder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contactReminderService.update).toHaveBeenCalledWith(contactReminder);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
