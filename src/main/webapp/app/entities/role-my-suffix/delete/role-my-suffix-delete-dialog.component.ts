import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoleMySuffix } from '../role-my-suffix.model';
import { RoleMySuffixService } from '../service/role-my-suffix.service';

@Component({
  templateUrl: './role-my-suffix-delete-dialog.component.html',
})
export class RoleMySuffixDeleteDialogComponent {
  role?: IRoleMySuffix;

  constructor(protected roleService: RoleMySuffixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
