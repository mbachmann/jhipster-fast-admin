import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentInvoiceWorkflowFaService } from '../service/document-invoice-workflow-fa.service';

import { DocumentInvoiceWorkflowFaComponent } from './document-invoice-workflow-fa.component';

describe('Component Tests', () => {
  describe('DocumentInvoiceWorkflowFa Management Component', () => {
    let comp: DocumentInvoiceWorkflowFaComponent;
    let fixture: ComponentFixture<DocumentInvoiceWorkflowFaComponent>;
    let service: DocumentInvoiceWorkflowFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentInvoiceWorkflowFaComponent],
      })
        .overrideTemplate(DocumentInvoiceWorkflowFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentInvoiceWorkflowFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentInvoiceWorkflowFaService);

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
      expect(comp.documentInvoiceWorkflows?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
