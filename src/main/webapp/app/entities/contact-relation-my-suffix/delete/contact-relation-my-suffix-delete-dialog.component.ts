import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactRelationMySuffix } from '../contact-relation-my-suffix.model';
import { ContactRelationMySuffixService } from '../service/contact-relation-my-suffix.service';

@Component({
  templateUrl: './contact-relation-my-suffix-delete-dialog.component.html',
})
export class ContactRelationMySuffixDeleteDialogComponent {
  contactRelation?: IContactRelationMySuffix;

  constructor(protected contactRelationService: ContactRelationMySuffixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactRelationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
