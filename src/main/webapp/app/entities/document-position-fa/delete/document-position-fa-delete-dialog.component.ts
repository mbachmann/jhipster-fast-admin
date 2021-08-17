import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentPositionFa } from '../document-position-fa.model';
import { DocumentPositionFaService } from '../service/document-position-fa.service';

@Component({
  templateUrl: './document-position-fa-delete-dialog.component.html',
})
export class DocumentPositionFaDeleteDialogComponent {
  documentPosition?: IDocumentPositionFa;

  constructor(protected documentPositionService: DocumentPositionFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentPositionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
