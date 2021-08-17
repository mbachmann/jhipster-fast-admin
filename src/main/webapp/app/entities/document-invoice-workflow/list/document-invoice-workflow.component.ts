import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentInvoiceWorkflow } from '../document-invoice-workflow.model';
import { DocumentInvoiceWorkflowService } from '../service/document-invoice-workflow.service';
import { DocumentInvoiceWorkflowDeleteDialogComponent } from '../delete/document-invoice-workflow-delete-dialog.component';

@Component({
  selector: 'fa-document-invoice-workflow',
  templateUrl: './document-invoice-workflow.component.html',
})
export class DocumentInvoiceWorkflowComponent implements OnInit {
  documentInvoiceWorkflows?: IDocumentInvoiceWorkflow[];
  isLoading = false;

  constructor(protected documentInvoiceWorkflowService: DocumentInvoiceWorkflowService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentInvoiceWorkflowService.query().subscribe(
      (res: HttpResponse<IDocumentInvoiceWorkflow[]>) => {
        this.isLoading = false;
        this.documentInvoiceWorkflows = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDocumentInvoiceWorkflow): number {
    return item.id!;
  }

  delete(documentInvoiceWorkflow: IDocumentInvoiceWorkflow): void {
    const modalRef = this.modalService.open(DocumentInvoiceWorkflowDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentInvoiceWorkflow = documentInvoiceWorkflow;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
