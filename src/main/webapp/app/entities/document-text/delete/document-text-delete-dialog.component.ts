import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentText } from '../document-text.model';
import { DocumentTextService } from '../service/document-text.service';

@Component({
  templateUrl: './document-text-delete-dialog.component.html',
})
export class DocumentTextDeleteDialogComponent {
  documentText?: IDocumentText;

  constructor(protected documentTextService: DocumentTextService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentTextService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
