import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentFreeTextService } from '../service/document-free-text.service';

import { DocumentFreeTextComponent } from './document-free-text.component';

describe('Component Tests', () => {
  describe('DocumentFreeText Management Component', () => {
    let comp: DocumentFreeTextComponent;
    let fixture: ComponentFixture<DocumentFreeTextComponent>;
    let service: DocumentFreeTextService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentFreeTextComponent],
      })
        .overrideTemplate(DocumentFreeTextComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentFreeTextComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentFreeTextService);

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
      expect(comp.documentFreeTexts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
