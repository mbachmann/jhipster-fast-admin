jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactRelationMySuffixService } from '../service/contact-relation-my-suffix.service';

import { ContactRelationMySuffixComponent } from './contact-relation-my-suffix.component';

describe('Component Tests', () => {
  describe('ContactRelationMySuffix Management Component', () => {
    let comp: ContactRelationMySuffixComponent;
    let fixture: ComponentFixture<ContactRelationMySuffixComponent>;
    let service: ContactRelationMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactRelationMySuffixComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(ContactRelationMySuffixComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactRelationMySuffixComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactRelationMySuffixService);

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
      expect(comp.contactRelations?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
