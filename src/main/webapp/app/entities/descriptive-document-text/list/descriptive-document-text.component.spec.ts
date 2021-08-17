import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DescriptiveDocumentTextService } from '../service/descriptive-document-text.service';

import { DescriptiveDocumentTextComponent } from './descriptive-document-text.component';

describe('Component Tests', () => {
  describe('DescriptiveDocumentText Management Component', () => {
    let comp: DescriptiveDocumentTextComponent;
    let fixture: ComponentFixture<DescriptiveDocumentTextComponent>;
    let service: DescriptiveDocumentTextService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DescriptiveDocumentTextComponent],
      })
        .overrideTemplate(DescriptiveDocumentTextComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DescriptiveDocumentTextComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DescriptiveDocumentTextService);

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
      expect(comp.descriptiveDocumentTexts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
