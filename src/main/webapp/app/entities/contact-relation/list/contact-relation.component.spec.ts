import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ContactRelationService } from '../service/contact-relation.service';

import { ContactRelationComponent } from './contact-relation.component';

describe('Component Tests', () => {
  describe('ContactRelation Management Component', () => {
    let comp: ContactRelationComponent;
    let fixture: ComponentFixture<ContactRelationComponent>;
    let service: ContactRelationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactRelationComponent],
      })
        .overrideTemplate(ContactRelationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactRelationComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactRelationService);

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
