import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPermissionFa } from '../permission-fa.model';
import { PermissionFaService } from '../service/permission-fa.service';

@Component({
  templateUrl: './permission-fa-delete-dialog.component.html',
})
export class PermissionFaDeleteDialogComponent {
  permission?: IPermissionFa;

  constructor(protected permissionService: PermissionFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.permissionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
