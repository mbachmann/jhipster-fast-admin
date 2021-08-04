jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactAddressMySuffixService } from '../service/contact-address-my-suffix.service';

import { ContactAddressMySuffixComponent } from './contact-address-my-suffix.component';

describe('Component Tests', () => {
  describe('ContactAddressMySuffix Management Component', () => {
    let comp: ContactAddressMySuffixComponent;
    let fixture: ComponentFixture<ContactAddressMySuffixComponent>;
    let service: ContactAddressMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactAddressMySuffixComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(ContactAddressMySuffixComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactAddressMySuffixComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactAddressMySuffixService);

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
      expect(comp.contactAddresses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
