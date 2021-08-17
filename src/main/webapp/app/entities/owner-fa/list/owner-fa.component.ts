import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOwnerFa } from '../owner-fa.model';
import { OwnerFaService } from '../service/owner-fa.service';
import { OwnerFaDeleteDialogComponent } from '../delete/owner-fa-delete-dialog.component';

@Component({
  selector: 'fa-owner-fa',
  templateUrl: './owner-fa.component.html',
})
export class OwnerFaComponent implements OnInit {
  owners?: IOwnerFa[];
  isLoading = false;

  constructor(protected ownerService: OwnerFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ownerService.query().subscribe(
      (res: HttpResponse<IOwnerFa[]>) => {
        this.isLoading = false;
        this.owners = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOwnerFa): number {
    return item.id!;
  }

  delete(owner: IOwnerFa): void {
    const modalRef = this.modalService.open(OwnerFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.owner = owner;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
