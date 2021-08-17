import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentFreeTextFaService } from '../service/document-free-text-fa.service';

import { DocumentFreeTextFaComponent } from './document-free-text-fa.component';

describe('Component Tests', () => {
  describe('DocumentFreeTextFa Management Component', () => {
    let comp: DocumentFreeTextFaComponent;
    let fixture: ComponentFixture<DocumentFreeTextFaComponent>;
    let service: DocumentFreeTextFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentFreeTextFaComponent],
      })
        .overrideTemplate(DocumentFreeTextFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentFreeTextFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentFreeTextFaService);

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
