jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContactReminderMySuffixService } from '../service/contact-reminder-my-suffix.service';
import { IContactReminderMySuffix, ContactReminderMySuffix } from '../contact-reminder-my-suffix.model';

import { ContactReminderMySuffixUpdateComponent } from './contact-reminder-my-suffix-update.component';

describe('Component Tests', () => {
  describe('ContactReminderMySuffix Management Update Component', () => {
    let comp: ContactReminderMySuffixUpdateComponent;
    let fixture: ComponentFixture<ContactReminderMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contactReminderService: ContactReminderMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactReminderMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContactReminderMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactReminderMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contactReminderService = TestBed.inject(ContactReminderMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const contactReminder: IContactReminderMySuffix = { id: 456 };

        activatedRoute.data = of({ contactReminder });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contactReminder));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContactReminderMySuffix>>();
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
        const saveSubject = new Subject<HttpResponse<ContactReminderMySuffix>>();
        const contactReminder = new ContactReminderMySuffix();
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
        const saveSubject = new Subject<HttpResponse<ContactReminderMySuffix>>();
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
