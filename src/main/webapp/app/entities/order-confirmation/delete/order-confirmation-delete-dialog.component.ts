import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderConfirmation } from '../order-confirmation.model';
import { OrderConfirmationService } from '../service/order-confirmation.service';

@Component({
  templateUrl: './order-confirmation-delete-dialog.component.html',
})
export class OrderConfirmationDeleteDialogComponent {
  orderConfirmation?: IOrderConfirmation;

  constructor(protected orderConfirmationService: OrderConfirmationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderConfirmationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
