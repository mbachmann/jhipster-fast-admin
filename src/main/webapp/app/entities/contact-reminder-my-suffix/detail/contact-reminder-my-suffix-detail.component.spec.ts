import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactReminderMySuffixDetailComponent } from './contact-reminder-my-suffix-detail.component';

describe('Component Tests', () => {
  describe('ContactReminderMySuffix Management Detail Component', () => {
    let comp: ContactReminderMySuffixDetailComponent;
    let fixture: ComponentFixture<ContactReminderMySuffixDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactReminderMySuffixDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactReminder: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactReminderMySuffixDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactReminderMySuffixDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactReminder on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactReminder).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
