import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentPositionService } from '../service/document-position.service';

import { DocumentPositionComponent } from './document-position.component';

describe('Component Tests', () => {
  describe('DocumentPosition Management Component', () => {
    let comp: DocumentPositionComponent;
    let fixture: ComponentFixture<DocumentPositionComponent>;
    let service: DocumentPositionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentPositionComponent],
      })
        .overrideTemplate(DocumentPositionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentPositionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentPositionService);

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
