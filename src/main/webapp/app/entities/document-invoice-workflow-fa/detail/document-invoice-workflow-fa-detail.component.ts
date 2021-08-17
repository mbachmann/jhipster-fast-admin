import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentInvoiceWorkflowFa } from '../document-invoice-workflow-fa.model';

@Component({
  selector: 'fa-document-invoice-workflow-fa-detail',
  templateUrl: './document-invoice-workflow-fa-detail.component.html',
})
export class DocumentInvoiceWorkflowFaDetailComponent implements OnInit {
  documentInvoiceWorkflow: IDocumentInvoiceWorkflowFa | null = null;

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
