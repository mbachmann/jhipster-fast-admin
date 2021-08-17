import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentTextService } from '../service/document-text.service';

import { DocumentTextComponent } from './document-text.component';

describe('Component Tests', () => {
  describe('DocumentText Management Component', () => {
    let comp: DocumentTextComponent;
    let fixture: ComponentFixture<DocumentTextComponent>;
    let service: DocumentTextService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentTextComponent],
      })
        .overrideTemplate(DocumentTextComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentTextComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentTextService);

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
