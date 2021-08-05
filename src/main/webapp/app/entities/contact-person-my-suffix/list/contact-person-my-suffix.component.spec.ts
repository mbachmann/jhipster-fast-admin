jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactPersonMySuffixService } from '../service/contact-person-my-suffix.service';

import { ContactPersonMySuffixComponent } from './contact-person-my-suffix.component';

describe('Component Tests', () => {
  describe('ContactPersonMySuffix Management Component', () => {
    let comp: ContactPersonMySuffixComponent;
    let fixture: ComponentFixture<ContactPersonMySuffixComponent>;
    let service: ContactPersonMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactPersonMySuffixComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(ContactPersonMySuffixComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactPersonMySuffixComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactPersonMySuffixService);

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
      expect(comp.contactPeople?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
