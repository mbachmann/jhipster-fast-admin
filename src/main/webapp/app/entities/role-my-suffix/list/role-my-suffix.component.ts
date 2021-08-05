import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoleMySuffix } from '../role-my-suffix.model';
import { RoleMySuffixService } from '../service/role-my-suffix.service';
import { RoleMySuffixDeleteDialogComponent } from '../delete/role-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-role-my-suffix',
  templateUrl: './role-my-suffix.component.html',
})
export class RoleMySuffixComponent implements OnInit {
  roles?: IRoleMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(protected roleService: RoleMySuffixService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.roleService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IRoleMySuffix[]>) => {
            this.isLoading = false;
            this.roles = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.roleService.query().subscribe(
      (res: HttpResponse<IRoleMySuffix[]>) => {
        this.isLoading = false;
        this.roles = res.body ?? [];
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

  trackId(index: number, item: IRoleMySuffix): number {
    return item.id!;
  }

  delete(role: IRoleMySuffix): void {
    const modalRef = this.modalService.open(RoleMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.role = role;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
