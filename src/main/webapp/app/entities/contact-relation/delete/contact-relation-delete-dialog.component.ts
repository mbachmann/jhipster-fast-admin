import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactRelation } from '../contact-relation.model';
import { ContactRelationService } from '../service/contact-relation.service';

@Component({
  templateUrl: './contact-relation-delete-dialog.component.html',
})
export class ContactRelationDeleteDialogComponent {
  contactRelation?: IContactRelation;

  constructor(protected contactRelationService: ContactRelationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactRelationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
