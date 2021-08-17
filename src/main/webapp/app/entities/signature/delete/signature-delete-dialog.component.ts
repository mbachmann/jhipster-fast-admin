import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISignature } from '../signature.model';
import { SignatureService } from '../service/signature.service';

@Component({
  templateUrl: './signature-delete-dialog.component.html',
})
export class SignatureDeleteDialogComponent {
  signature?: ISignature;

  constructor(protected signatureService: SignatureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.signatureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
