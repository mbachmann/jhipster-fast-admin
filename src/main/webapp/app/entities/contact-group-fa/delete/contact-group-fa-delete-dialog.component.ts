import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactGroupFa } from '../contact-group-fa.model';
import { ContactGroupFaService } from '../service/contact-group-fa.service';

@Component({
  templateUrl: './contact-group-fa-delete-dialog.component.html',
})
export class ContactGroupFaDeleteDialogComponent {
  contactGroup?: IContactGroupFa;

  constructor(protected contactGroupService: ContactGroupFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactGroupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
