import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ContactRelationFaService } from '../service/contact-relation-fa.service';

import { ContactRelationFaComponent } from './contact-relation-fa.component';

describe('Component Tests', () => {
  describe('ContactRelationFa Management Component', () => {
    let comp: ContactRelationFaComponent;
    let fixture: ComponentFixture<ContactRelationFaComponent>;
    let service: ContactRelationFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContactRelationFaComponent],
      })
        .overrideTemplate(ContactRelationFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactRelationFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContactRelationFaService);

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
