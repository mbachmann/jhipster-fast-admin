jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactReminderMySuffixService } from '../service/contact-reminder-my-suffix.service';

import { ContactReminderMySuffixComponent } from './contact-reminder-my-suffix.component';

describe('Component Tests', () => {
  describe('ContactReminderMySuffix Management Component', () => {
    let comp: ContactReminderMySuffixComponent;
    let fixture: ComponentFixture<ContactReminderMySuffixComponent>;
    let service: ContactReminderMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactReminderMySuffixComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(ContactReminderMySuffixComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactReminderMySuffixComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactReminderMySuffixService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.contactReminders?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
