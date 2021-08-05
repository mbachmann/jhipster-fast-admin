import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactMySuffix } from '../contact-my-suffix.model';
import { ContactMySuffixService } from '../service/contact-my-suffix.service';

@Component({
  templateUrl: './contact-my-suffix-delete-dialog.component.html',
})
export class ContactMySuffixDeleteDialogComponent {
  contact?: IContactMySuffix;

  constructor(protected contactService: ContactMySuffixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
