jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactPersonFaService } from '../service/contact-person-fa.service';

import { ContactPersonFaComponent } from './contact-person-fa.component';

describe('Component Tests', () => {
  describe('ContactPersonFa Management Component', () => {
    let comp: ContactPersonFaComponent;
    let fixture: ComponentFixture<ContactPersonFaComponent>;
    let service: ContactPersonFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactPersonFaComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(ContactPersonFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactPersonFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactPersonFaService);

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
