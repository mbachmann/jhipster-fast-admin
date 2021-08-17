import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderConfirmationFa } from '../order-confirmation-fa.model';
import { OrderConfirmationFaService } from '../service/order-confirmation-fa.service';

@Component({
  templateUrl: './order-confirmation-fa-delete-dialog.component.html',
})
export class OrderConfirmationFaDeleteDialogComponent {
  orderConfirmation?: IOrderConfirmationFa;

  constructor(protected orderConfirmationService: OrderConfirmationFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderConfirmationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
