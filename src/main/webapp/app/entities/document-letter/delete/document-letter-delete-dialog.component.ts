import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentLetter } from '../document-letter.model';
import { DocumentLetterService } from '../service/document-letter.service';

@Component({
  templateUrl: './document-letter-delete-dialog.component.html',
})
export class DocumentLetterDeleteDialogComponent {
  documentLetter?: IDocumentLetter;

  constructor(protected documentLetterService: DocumentLetterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentLetterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
