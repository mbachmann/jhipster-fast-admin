import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactReminderDetailComponent } from './contact-reminder-detail.component';

describe('Component Tests', () => {
  describe('ContactReminder Management Detail Component', () => {
    let comp: ContactReminderDetailComponent;
    let fixture: ComponentFixture<ContactReminderDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactReminderDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactReminder: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactReminderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactReminderDetailComponent);
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
