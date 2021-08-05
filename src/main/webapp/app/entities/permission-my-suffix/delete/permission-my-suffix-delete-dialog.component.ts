import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPermissionMySuffix } from '../permission-my-suffix.model';
import { PermissionMySuffixService } from '../service/permission-my-suffix.service';

@Component({
  templateUrl: './permission-my-suffix-delete-dialog.component.html',
})
export class PermissionMySuffixDeleteDialogComponent {
  permission?: IPermissionMySuffix;

  constructor(protected permissionService: PermissionMySuffixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.permissionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
