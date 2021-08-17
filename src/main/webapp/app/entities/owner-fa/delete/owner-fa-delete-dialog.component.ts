import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOwnerFa } from '../owner-fa.model';
import { OwnerFaService } from '../service/owner-fa.service';

@Component({
  templateUrl: './owner-fa-delete-dialog.component.html',
})
export class OwnerFaDeleteDialogComponent {
  owner?: IOwnerFa;

  constructor(protected ownerService: OwnerFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ownerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
