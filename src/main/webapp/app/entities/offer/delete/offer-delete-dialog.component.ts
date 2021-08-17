import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOffer } from '../offer.model';
import { OfferService } from '../service/offer.service';

@Component({
  templateUrl: './offer-delete-dialog.component.html',
})
export class OfferDeleteDialogComponent {
  offer?: IOffer;

  constructor(protected offerService: OfferService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.offerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
