jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ContactReminderService } from '../service/contact-reminder.service';

import { ContactReminderDeleteDialogComponent } from './contact-reminder-delete-dialog.component';

describe('Component Tests', () => {
  describe('ContactReminder Management Delete Component', () => {
    let comp: ContactReminderDeleteDialogComponent;
    let fixture: ComponentFixture<ContactReminderDeleteDialogComponent>;
    let service: ContactReminderService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactReminderDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ContactReminderDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactReminderDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactReminderService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
