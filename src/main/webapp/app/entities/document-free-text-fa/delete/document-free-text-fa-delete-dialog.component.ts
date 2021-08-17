import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentFreeTextFa } from '../document-free-text-fa.model';
import { DocumentFreeTextFaService } from '../service/document-free-text-fa.service';

@Component({
  templateUrl: './document-free-text-fa-delete-dialog.component.html',
})
export class DocumentFreeTextFaDeleteDialogComponent {
  documentFreeText?: IDocumentFreeTextFa;

  constructor(protected documentFreeTextService: DocumentFreeTextFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentFreeTextService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
