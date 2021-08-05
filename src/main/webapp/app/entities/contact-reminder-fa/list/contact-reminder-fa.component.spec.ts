jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactReminderFaService } from '../service/contact-reminder-fa.service';

import { ContactReminderFaComponent } from './contact-reminder-fa.component';

describe('Component Tests', () => {
  describe('ContactReminderFa Management Component', () => {
    let comp: ContactReminderFaComponent;
    let fixture: ComponentFixture<ContactReminderFaComponent>;
    let service: ContactReminderFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactReminderFaComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(ContactReminderFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactReminderFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactReminderFaService);

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
