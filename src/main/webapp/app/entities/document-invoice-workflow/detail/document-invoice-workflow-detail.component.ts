import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentInvoiceWorkflow } from '../document-invoice-workflow.model';

@Component({
  selector: 'fa-document-invoice-workflow-detail',
  templateUrl: './document-invoice-workflow-detail.component.html',
})
export class DocumentInvoiceWorkflowDetailComponent implements OnInit {
  documentInvoiceWorkflow: IDocumentInvoiceWorkflow | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentInvoiceWorkflow }) => {
      this.documentInvoiceWorkflow = documentInvoiceWorkflow;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
