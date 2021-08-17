import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactPersonFa } from '../contact-person-fa.model';
import { ContactPersonFaService } from '../service/contact-person-fa.service';

@Component({
  templateUrl: './contact-person-fa-delete-dialog.component.html',
})
export class ContactPersonFaDeleteDialogComponent {
  contactPerson?: IContactPersonFa;

  constructor(protected contactPersonService: ContactPersonFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactPersonService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
