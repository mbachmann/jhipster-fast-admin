import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentPosition } from '../document-position.model';
import { DocumentPositionService } from '../service/document-position.service';

@Component({
  templateUrl: './document-position-delete-dialog.component.html',
})
export class DocumentPositionDeleteDialogComponent {
  documentPosition?: IDocumentPosition;

  constructor(protected documentPositionService: DocumentPositionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentPositionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
