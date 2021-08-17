import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPermission } from '../permission.model';
import { PermissionService } from '../service/permission.service';
import { PermissionDeleteDialogComponent } from '../delete/permission-delete-dialog.component';

@Component({
  selector: 'fa-permission',
  templateUrl: './permission.component.html',
})
export class PermissionComponent implements OnInit {
  permissions?: IPermission[];
  isLoading = false;

  constructor(protected permissionService: PermissionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.permissionService.query().subscribe(
      (res: HttpResponse<IPermission[]>) => {
        this.isLoading = false;
        this.permissions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPermission): number {
    return item.id!;
  }

  delete(permission: IPermission): void {
    const modalRef = this.modalService.open(PermissionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.permission = permission;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
