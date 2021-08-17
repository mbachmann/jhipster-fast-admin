import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentInvoiceWorkflowDetailComponent } from './document-invoice-workflow-detail.component';

describe('Component Tests', () => {
  describe('DocumentInvoiceWorkflow Management Detail Component', () => {
    let comp: DocumentInvoiceWorkflowDetailComponent;
    let fixture: ComponentFixture<DocumentInvoiceWorkflowDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentInvoiceWorkflowDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentInvoiceWorkflow: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentInvoiceWorkflowDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentInvoiceWorkflowDetailComponent);
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
