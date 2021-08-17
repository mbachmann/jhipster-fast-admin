import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFreeTextFa } from '../free-text-fa.model';
import { FreeTextFaService } from '../service/free-text-fa.service';

@Component({
  templateUrl: './free-text-fa-delete-dialog.component.html',
})
export class FreeTextFaDeleteDialogComponent {
  freeText?: IFreeTextFa;

  constructor(protected freeTextService: FreeTextFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.freeTextService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
