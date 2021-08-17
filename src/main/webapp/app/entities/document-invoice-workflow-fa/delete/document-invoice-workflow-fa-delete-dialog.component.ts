import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentInvoiceWorkflowFa } from '../document-invoice-workflow-fa.model';
import { DocumentInvoiceWorkflowFaService } from '../service/document-invoice-workflow-fa.service';

@Component({
  templateUrl: './document-invoice-workflow-fa-delete-dialog.component.html',
})
export class DocumentInvoiceWorkflowFaDeleteDialogComponent {
  documentInvoiceWorkflow?: IDocumentInvoiceWorkflowFa;

  constructor(protected documentInvoiceWorkflowService: DocumentInvoiceWorkflowFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentInvoiceWorkflowService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
