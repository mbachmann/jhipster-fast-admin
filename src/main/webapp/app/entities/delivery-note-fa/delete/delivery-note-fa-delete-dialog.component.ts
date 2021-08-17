import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeliveryNoteFa } from '../delivery-note-fa.model';
import { DeliveryNoteFaService } from '../service/delivery-note-fa.service';

@Component({
  templateUrl: './delivery-note-fa-delete-dialog.component.html',
})
export class DeliveryNoteFaDeleteDialogComponent {
  deliveryNote?: IDeliveryNoteFa;

  constructor(protected deliveryNoteService: DeliveryNoteFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deliveryNoteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
