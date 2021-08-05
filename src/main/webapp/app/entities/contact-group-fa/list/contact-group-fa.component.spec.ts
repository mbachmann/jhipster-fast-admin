jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactGroupFaService } from '../service/contact-group-fa.service';

import { ContactGroupFaComponent } from './contact-group-fa.component';

describe('Component Tests', () => {
  describe('ContactGroupFa Management Component', () => {
    let comp: ContactGroupFaComponent;
    let fixture: ComponentFixture<ContactGroupFaComponent>;
    let service: ContactGroupFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactGroupFaComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(ContactGroupFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactGroupFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactGroupFaService);

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
      expect(comp.contactGroups?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
