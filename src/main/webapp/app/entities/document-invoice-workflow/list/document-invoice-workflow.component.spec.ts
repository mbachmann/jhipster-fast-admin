import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentInvoiceWorkflowService } from '../service/document-invoice-workflow.service';

import { DocumentInvoiceWorkflowComponent } from './document-invoice-workflow.component';

describe('Component Tests', () => {
  describe('DocumentInvoiceWorkflow Management Component', () => {
    let comp: DocumentInvoiceWorkflowComponent;
    let fixture: ComponentFixture<DocumentInvoiceWorkflowComponent>;
    let service: DocumentInvoiceWorkflowService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentInvoiceWorkflowComponent],
      })
        .overrideTemplate(DocumentInvoiceWorkflowComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentInvoiceWorkflowComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentInvoiceWorkflowService);

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
