import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IResourcePermission } from '../resource-permission.model';
import { ResourcePermissionService } from '../service/resource-permission.service';
import { ResourcePermissionDeleteDialogComponent } from '../delete/resource-permission-delete-dialog.component';

@Component({
  selector: 'fa-resource-permission',
  templateUrl: './resource-permission.component.html',
})
export class ResourcePermissionComponent implements OnInit {
  resourcePermissions?: IResourcePermission[];
  isLoading = false;

  constructor(protected resourcePermissionService: ResourcePermissionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.resourcePermissionService.query().subscribe(
      (res: HttpResponse<IResourcePermission[]>) => {
        this.isLoading = false;
        this.resourcePermissions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IResourcePermission): number {
    return item.id!;
  }

  delete(resourcePermission: IResourcePermission): void {
    const modalRef = this.modalService.open(ResourcePermissionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.resourcePermission = resourcePermission;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
