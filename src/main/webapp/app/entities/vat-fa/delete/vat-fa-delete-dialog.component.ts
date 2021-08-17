import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVatFa } from '../vat-fa.model';
import { VatFaService } from '../service/vat-fa.service';

@Component({
  templateUrl: './vat-fa-delete-dialog.component.html',
})
export class VatFaDeleteDialogComponent {
  vat?: IVatFa;

  constructor(protected vatService: VatFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
