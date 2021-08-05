import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOwnerMySuffix } from '../owner-my-suffix.model';
import { OwnerMySuffixService } from '../service/owner-my-suffix.service';
import { OwnerMySuffixDeleteDialogComponent } from '../delete/owner-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-owner-my-suffix',
  templateUrl: './owner-my-suffix.component.html',
})
export class OwnerMySuffixComponent implements OnInit {
  owners?: IOwnerMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(protected ownerService: OwnerMySuffixService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.ownerService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IOwnerMySuffix[]>) => {
            this.isLoading = false;
            this.owners = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.ownerService.query().subscribe(
      (res: HttpResponse<IOwnerMySuffix[]>) => {
        this.isLoading = false;
        this.owners = res.body ?? [];
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

  trackId(index: number, item: IOwnerMySuffix): number {
    return item.id!;
  }

  delete(owner: IOwnerMySuffix): void {
    const modalRef = this.modalService.open(OwnerMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.owner = owner;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
