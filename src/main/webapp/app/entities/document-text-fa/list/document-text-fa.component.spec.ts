import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentTextFaService } from '../service/document-text-fa.service';

import { DocumentTextFaComponent } from './document-text-fa.component';

describe('Component Tests', () => {
  describe('DocumentTextFa Management Component', () => {
    let comp: DocumentTextFaComponent;
    let fixture: ComponentFixture<DocumentTextFaComponent>;
    let service: DocumentTextFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentTextFaComponent],
      })
        .overrideTemplate(DocumentTextFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentTextFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentTextFaService);

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
      expect(comp.documentTexts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
