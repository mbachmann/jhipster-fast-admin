jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactGroupMySuffixService } from '../service/contact-group-my-suffix.service';

import { ContactGroupMySuffixComponent } from './contact-group-my-suffix.component';

describe('Component Tests', () => {
  describe('ContactGroupMySuffix Management Component', () => {
    let comp: ContactGroupMySuffixComponent;
    let fixture: ComponentFixture<ContactGroupMySuffixComponent>;
    let service: ContactGroupMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactGroupMySuffixComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(ContactGroupMySuffixComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactGroupMySuffixComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactGroupMySuffixService);

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
