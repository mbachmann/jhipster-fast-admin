import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApplicationRole } from '../application-role.model';
import { ApplicationRoleService } from '../service/application-role.service';

@Component({
  templateUrl: './application-role-delete-dialog.component.html',
})
export class ApplicationRoleDeleteDialogComponent {
  applicationRole?: IApplicationRole;

  constructor(protected applicationRoleService: ApplicationRoleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.applicationRoleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
