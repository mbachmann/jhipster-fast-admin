import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactGroupMySuffix } from '../contact-group-my-suffix.model';
import { ContactGroupMySuffixService } from '../service/contact-group-my-suffix.service';

@Component({
  templateUrl: './contact-group-my-suffix-delete-dialog.component.html',
})
export class ContactGroupMySuffixDeleteDialogComponent {
  contactGroup?: IContactGroupMySuffix;

  constructor(protected contactGroupService: ContactGroupMySuffixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactGroupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
