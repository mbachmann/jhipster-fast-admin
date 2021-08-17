import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResourcePermission } from '../resource-permission.model';
import { ResourcePermissionService } from '../service/resource-permission.service';

@Component({
  templateUrl: './resource-permission-delete-dialog.component.html',
})
export class ResourcePermissionDeleteDialogComponent {
  resourcePermission?: IResourcePermission;

  constructor(protected resourcePermissionService: ResourcePermissionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resourcePermissionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
