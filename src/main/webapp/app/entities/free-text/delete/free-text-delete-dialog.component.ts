import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFreeText } from '../free-text.model';
import { FreeTextService } from '../service/free-text.service';

@Component({
  templateUrl: './free-text-delete-dialog.component.html',
})
export class FreeTextDeleteDialogComponent {
  freeText?: IFreeText;

  constructor(protected freeTextService: FreeTextService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.freeTextService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
