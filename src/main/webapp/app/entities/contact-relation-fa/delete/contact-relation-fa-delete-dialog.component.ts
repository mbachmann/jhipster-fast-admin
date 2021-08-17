import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactRelationFa } from '../contact-relation-fa.model';
import { ContactRelationFaService } from '../service/contact-relation-fa.service';

@Component({
  templateUrl: './contact-relation-fa-delete-dialog.component.html',
})
export class ContactRelationFaDeleteDialogComponent {
  contactRelation?: IContactRelationFa;

  constructor(protected contactRelationService: ContactRelationFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactRelationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
