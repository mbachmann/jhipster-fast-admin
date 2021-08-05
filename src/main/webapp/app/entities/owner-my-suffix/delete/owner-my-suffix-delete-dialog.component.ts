import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOwnerMySuffix } from '../owner-my-suffix.model';
import { OwnerMySuffixService } from '../service/owner-my-suffix.service';

@Component({
  templateUrl: './owner-my-suffix-delete-dialog.component.html',
})
export class OwnerMySuffixDeleteDialogComponent {
  owner?: IOwnerMySuffix;

  constructor(protected ownerService: OwnerMySuffixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ownerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
