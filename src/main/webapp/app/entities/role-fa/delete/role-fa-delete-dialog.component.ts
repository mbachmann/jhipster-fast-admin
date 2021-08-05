import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoleFa } from '../role-fa.model';
import { RoleFaService } from '../service/role-fa.service';

@Component({
  templateUrl: './role-fa-delete-dialog.component.html',
})
export class RoleFaDeleteDialogComponent {
  role?: IRoleFa;

  constructor(protected roleService: RoleFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
