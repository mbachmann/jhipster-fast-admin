import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentTextFa } from '../document-text-fa.model';
import { DocumentTextFaService } from '../service/document-text-fa.service';

@Component({
  templateUrl: './document-text-fa-delete-dialog.component.html',
})
export class DocumentTextFaDeleteDialogComponent {
  documentText?: IDocumentTextFa;

  constructor(protected documentTextService: DocumentTextFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentTextService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
