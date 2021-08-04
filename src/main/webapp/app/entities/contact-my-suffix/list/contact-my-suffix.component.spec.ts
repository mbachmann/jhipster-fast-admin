jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactMySuffixService } from '../service/contact-my-suffix.service';

import { ContactMySuffixComponent } from './contact-my-suffix.component';

describe('Component Tests', () => {
  describe('ContactMySuffix Management Component', () => {
    let comp: ContactMySuffixComponent;
    let fixture: ComponentFixture<ContactMySuffixComponent>;
    let service: ContactMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactMySuffixComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(ContactMySuffixComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactMySuffixComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactMySuffixService);

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
      expect(comp.contacts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
