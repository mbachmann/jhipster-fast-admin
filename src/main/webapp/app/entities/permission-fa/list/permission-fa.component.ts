import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPermissionFa } from '../permission-fa.model';
import { PermissionFaService } from '../service/permission-fa.service';
import { PermissionFaDeleteDialogComponent } from '../delete/permission-fa-delete-dialog.component';

@Component({
  selector: 'fa-permission-fa',
  templateUrl: './permission-fa.component.html',
})
export class PermissionFaComponent implements OnInit {
  permissions?: IPermissionFa[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected permissionService: PermissionFaService,
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
          (res: HttpResponse<IPermissionFa[]>) => {
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
      (res: HttpResponse<IPermissionFa[]>) => {
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

  trackId(index: number, item: IPermissionFa): number {
    return item.id!;
  }

  delete(permission: IPermissionFa): void {
    const modalRef = this.modalService.open(PermissionFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.permission = permission;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
