import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactAddressMySuffix } from '../contact-address-my-suffix.model';
import { ContactAddressMySuffixService } from '../service/contact-address-my-suffix.service';

@Component({
  templateUrl: './contact-address-my-suffix-delete-dialog.component.html',
})
export class ContactAddressMySuffixDeleteDialogComponent {
  contactAddress?: IContactAddressMySuffix;

  constructor(protected contactAddressService: ContactAddressMySuffixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactAddressService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
