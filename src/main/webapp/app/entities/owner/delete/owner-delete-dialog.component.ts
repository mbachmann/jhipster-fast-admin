import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOwner } from '../owner.model';
import { OwnerService } from '../service/owner.service';

@Component({
  templateUrl: './owner-delete-dialog.component.html',
})
export class OwnerDeleteDialogComponent {
  owner?: IOwner;

  constructor(protected ownerService: OwnerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ownerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
