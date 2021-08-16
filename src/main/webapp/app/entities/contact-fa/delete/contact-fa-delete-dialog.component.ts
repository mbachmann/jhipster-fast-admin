import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactFa } from '../contact-fa.model';
import { ContactFaService } from '../service/contact-fa.service';

@Component({
  templateUrl: './contact-fa-delete-dialog.component.html',
})
export class ContactFaDeleteDialogComponent {
  contact?: IContactFa;

  constructor(protected contactService: ContactFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
