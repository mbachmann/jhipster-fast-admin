import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOfferFa } from '../offer-fa.model';
import { OfferFaService } from '../service/offer-fa.service';

@Component({
  templateUrl: './offer-fa-delete-dialog.component.html',
})
export class OfferFaDeleteDialogComponent {
  offer?: IOfferFa;

  constructor(protected offerService: OfferFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.offerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
