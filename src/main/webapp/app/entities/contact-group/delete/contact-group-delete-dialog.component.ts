import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactGroup } from '../contact-group.model';
import { ContactGroupService } from '../service/contact-group.service';

@Component({
  templateUrl: './contact-group-delete-dialog.component.html',
})
export class ContactGroupDeleteDialogComponent {
  contactGroup?: IContactGroup;

  constructor(protected contactGroupService: ContactGroupService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactGroupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
