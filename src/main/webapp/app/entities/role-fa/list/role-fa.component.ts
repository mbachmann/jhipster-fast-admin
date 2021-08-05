import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoleFa } from '../role-fa.model';
import { RoleFaService } from '../service/role-fa.service';
import { RoleFaDeleteDialogComponent } from '../delete/role-fa-delete-dialog.component';

@Component({
  selector: 'fa-role-fa',
  templateUrl: './role-fa.component.html',
})
export class RoleFaComponent implements OnInit {
  roles?: IRoleFa[];
  isLoading = false;
  currentSearch: string;

  constructor(protected roleService: RoleFaService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
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
          (res: HttpResponse<IRoleFa[]>) => {
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
      (res: HttpResponse<IRoleFa[]>) => {
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

  trackId(index: number, item: IRoleFa): number {
    return item.id!;
  }

  delete(role: IRoleFa): void {
    const modalRef = this.modalService.open(RoleFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.role = role;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
