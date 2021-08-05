import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactAddressFa } from '../contact-address-fa.model';
import { ContactAddressFaService } from '../service/contact-address-fa.service';

@Component({
  templateUrl: './contact-address-fa-delete-dialog.component.html',
})
export class ContactAddressFaDeleteDialogComponent {
  contactAddress?: IContactAddressFa;

  constructor(protected contactAddressService: ContactAddressFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactAddressService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
