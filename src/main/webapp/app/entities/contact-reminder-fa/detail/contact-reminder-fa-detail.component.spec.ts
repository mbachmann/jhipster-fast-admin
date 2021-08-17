import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactReminderFaDetailComponent } from './contact-reminder-fa-detail.component';

describe('Component Tests', () => {
  describe('ContactReminderFa Management Detail Component', () => {
    let comp: ContactReminderFaDetailComponent;
    let fixture: ComponentFixture<ContactReminderFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactReminderFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactReminder: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactReminderFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactReminderFaDetailComponent);
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
