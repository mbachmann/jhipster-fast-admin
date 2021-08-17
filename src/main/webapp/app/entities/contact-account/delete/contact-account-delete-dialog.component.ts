import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactAccount } from '../contact-account.model';
import { ContactAccountService } from '../service/contact-account.service';

@Component({
  templateUrl: './contact-account-delete-dialog.component.html',
})
export class ContactAccountDeleteDialogComponent {
  contactAccount?: IContactAccount;

  constructor(protected contactAccountService: ContactAccountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactAccountService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
