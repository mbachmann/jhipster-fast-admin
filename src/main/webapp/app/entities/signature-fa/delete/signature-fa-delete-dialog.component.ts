import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISignatureFa } from '../signature-fa.model';
import { SignatureFaService } from '../service/signature-fa.service';

@Component({
  templateUrl: './signature-fa-delete-dialog.component.html',
})
export class SignatureFaDeleteDialogComponent {
  signature?: ISignatureFa;

  constructor(protected signatureService: SignatureFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.signatureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
