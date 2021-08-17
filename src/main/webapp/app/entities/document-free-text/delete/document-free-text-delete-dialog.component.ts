import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentFreeText } from '../document-free-text.model';
import { DocumentFreeTextService } from '../service/document-free-text.service';

@Component({
  templateUrl: './document-free-text-delete-dialog.component.html',
})
export class DocumentFreeTextDeleteDialogComponent {
  documentFreeText?: IDocumentFreeText;

  constructor(protected documentFreeTextService: DocumentFreeTextService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentFreeTextService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
