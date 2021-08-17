import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentPositionFaService } from '../service/document-position-fa.service';

import { DocumentPositionFaComponent } from './document-position-fa.component';

describe('Component Tests', () => {
  describe('DocumentPositionFa Management Component', () => {
    let comp: DocumentPositionFaComponent;
    let fixture: ComponentFixture<DocumentPositionFaComponent>;
    let service: DocumentPositionFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentPositionFaComponent],
      })
        .overrideTemplate(DocumentPositionFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentPositionFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentPositionFaService);

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
      expect(comp.documentPositions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
