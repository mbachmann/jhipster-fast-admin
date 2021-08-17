import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvoiceFa } from '../invoice-fa.model';
import { InvoiceFaService } from '../service/invoice-fa.service';

@Component({
  templateUrl: './invoice-fa-delete-dialog.component.html',
})
export class InvoiceFaDeleteDialogComponent {
  invoice?: IInvoiceFa;

  constructor(protected invoiceService: InvoiceFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.invoiceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
