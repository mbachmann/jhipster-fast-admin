import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentInvoiceWorkflow } from '../document-invoice-workflow.model';
import { DocumentInvoiceWorkflowService } from '../service/document-invoice-workflow.service';

@Component({
  templateUrl: './document-invoice-workflow-delete-dialog.component.html',
})
export class DocumentInvoiceWorkflowDeleteDialogComponent {
  documentInvoiceWorkflow?: IDocumentInvoiceWorkflow;

  constructor(protected documentInvoiceWorkflowService: DocumentInvoiceWorkflowService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentInvoiceWorkflowService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
