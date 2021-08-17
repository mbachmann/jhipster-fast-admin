import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIsr } from '../isr.model';
import { IsrService } from '../service/isr.service';
import { IsrDeleteDialogComponent } from '../delete/isr-delete-dialog.component';

@Component({
  selector: 'fa-isr',
  templateUrl: './isr.component.html',
})
export class IsrComponent implements OnInit {
  isrs?: IIsr[];
  isLoading = false;

  constructor(protected isrService: IsrService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.isrService.query().subscribe(
      (res: HttpResponse<IIsr[]>) => {
        this.isLoading = false;
        this.isrs = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IIsr): number {
    return item.id!;
  }

  delete(isr: IIsr): void {
    const modalRef = this.modalService.open(IsrDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.isr = isr;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
