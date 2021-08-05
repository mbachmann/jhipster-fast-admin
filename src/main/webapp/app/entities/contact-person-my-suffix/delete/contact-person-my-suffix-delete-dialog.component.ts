import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactPersonMySuffix } from '../contact-person-my-suffix.model';
import { ContactPersonMySuffixService } from '../service/contact-person-my-suffix.service';

@Component({
  templateUrl: './contact-person-my-suffix-delete-dialog.component.html',
})
export class ContactPersonMySuffixDeleteDialogComponent {
  contactPerson?: IContactPersonMySuffix;

  constructor(protected contactPersonService: ContactPersonMySuffixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactPersonService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
