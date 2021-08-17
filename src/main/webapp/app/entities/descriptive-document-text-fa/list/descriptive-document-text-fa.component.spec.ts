import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DescriptiveDocumentTextFaService } from '../service/descriptive-document-text-fa.service';

import { DescriptiveDocumentTextFaComponent } from './descriptive-document-text-fa.component';

describe('Component Tests', () => {
  describe('DescriptiveDocumentTextFa Management Component', () => {
    let comp: DescriptiveDocumentTextFaComponent;
    let fixture: ComponentFixture<DescriptiveDocumentTextFaComponent>;
    let service: DescriptiveDocumentTextFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DescriptiveDocumentTextFaComponent],
      })
        .overrideTemplate(DescriptiveDocumentTextFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DescriptiveDocumentTextFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DescriptiveDocumentTextFaService);

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
