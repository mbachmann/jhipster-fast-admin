import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentInvoiceWorkflowFaDetailComponent } from './document-invoice-workflow-fa-detail.component';

describe('Component Tests', () => {
  describe('DocumentInvoiceWorkflowFa Management Detail Component', () => {
    let comp: DocumentInvoiceWorkflowFaDetailComponent;
    let fixture: ComponentFixture<DocumentInvoiceWorkflowFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentInvoiceWorkflowFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentInvoiceWorkflow: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentInvoiceWorkflowFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentInvoiceWorkflowFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load documentInvoiceWorkflow on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documentInvoiceWorkflow).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
