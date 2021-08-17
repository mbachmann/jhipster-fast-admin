import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactAddress } from '../contact-address.model';
import { ContactAddressService } from '../service/contact-address.service';

@Component({
  templateUrl: './contact-address-delete-dialog.component.html',
})
export class ContactAddressDeleteDialogComponent {
  contactAddress?: IContactAddress;

  constructor(protected contactAddressService: ContactAddressService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactAddressService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
