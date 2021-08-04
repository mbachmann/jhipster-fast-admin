import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPermissionMySuffix } from '../permission-my-suffix.model';
import { PermissionMySuffixService } from '../service/permission-my-suffix.service';
import { PermissionMySuffixDeleteDialogComponent } from '../delete/permission-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-permission-my-suffix',
  templateUrl: './permission-my-suffix.component.html',
})
export class PermissionMySuffixComponent implements OnInit {
  permissions?: IPermissionMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected permissionService: PermissionMySuffixService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.permissionService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IPermissionMySuffix[]>) => {
            this.isLoading = false;
            this.permissions = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.permissionService.query().subscribe(
      (res: HttpResponse<IPermissionMySuffix[]>) => {
        this.isLoading = false;
        this.permissions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPermissionMySuffix): number {
    return item.id!;
  }

  delete(permission: IPermissionMySuffix): void {
    const modalRef = this.modalService.open(PermissionMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.permission = permission;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
