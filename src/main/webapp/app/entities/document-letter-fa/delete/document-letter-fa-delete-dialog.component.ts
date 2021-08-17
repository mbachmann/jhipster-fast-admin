import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentLetterFa } from '../document-letter-fa.model';
import { DocumentLetterFaService } from '../service/document-letter-fa.service';

@Component({
  templateUrl: './document-letter-fa-delete-dialog.component.html',
})
export class DocumentLetterFaDeleteDialogComponent {
  documentLetter?: IDocumentLetterFa;

  constructor(protected documentLetterService: DocumentLetterFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentLetterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
