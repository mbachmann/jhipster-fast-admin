import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBankAccountFa } from '../bank-account-fa.model';
import { BankAccountFaService } from '../service/bank-account-fa.service';

@Component({
  templateUrl: './bank-account-fa-delete-dialog.component.html',
})
export class BankAccountFaDeleteDialogComponent {
  bankAccount?: IBankAccountFa;

  constructor(protected bankAccountService: BankAccountFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bankAccountService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
