import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentInvoiceWorkflowFa } from '../document-invoice-workflow-fa.model';
import { DocumentInvoiceWorkflowFaService } from '../service/document-invoice-workflow-fa.service';
import { DocumentInvoiceWorkflowFaDeleteDialogComponent } from '../delete/document-invoice-workflow-fa-delete-dialog.component';

@Component({
  selector: 'fa-document-invoice-workflow-fa',
  templateUrl: './document-invoice-workflow-fa.component.html',
})
export class DocumentInvoiceWorkflowFaComponent implements OnInit {
  documentInvoiceWorkflows?: IDocumentInvoiceWorkflowFa[];
  isLoading = false;

  constructor(protected documentInvoiceWorkflowService: DocumentInvoiceWorkflowFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentInvoiceWorkflowService.query().subscribe(
      (res: HttpResponse<IDocumentInvoiceWorkflowFa[]>) => {
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

  trackId(index: number, item: IDocumentInvoiceWorkflowFa): number {
    return item.id!;
  }

  delete(documentInvoiceWorkflow: IDocumentInvoiceWorkflowFa): void {
    const modalRef = this.modalService.open(DocumentInvoiceWorkflowFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentInvoiceWorkflow = documentInvoiceWorkflow;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
