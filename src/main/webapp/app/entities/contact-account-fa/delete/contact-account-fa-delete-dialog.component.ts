import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactAccountFa } from '../contact-account-fa.model';
import { ContactAccountFaService } from '../service/contact-account-fa.service';

@Component({
  templateUrl: './contact-account-fa-delete-dialog.component.html',
})
export class ContactAccountFaDeleteDialogComponent {
  contactAccount?: IContactAccountFa;

  constructor(protected contactAccountService: ContactAccountFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactAccountService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
