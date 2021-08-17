import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIsrFa } from '../isr-fa.model';
import { IsrFaService } from '../service/isr-fa.service';
import { IsrFaDeleteDialogComponent } from '../delete/isr-fa-delete-dialog.component';

@Component({
  selector: 'fa-isr-fa',
  templateUrl: './isr-fa.component.html',
})
export class IsrFaComponent implements OnInit {
  isrs?: IIsrFa[];
  isLoading = false;

  constructor(protected isrService: IsrFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.isrService.query().subscribe(
      (res: HttpResponse<IIsrFa[]>) => {
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

  trackId(index: number, item: IIsrFa): number {
    return item.id!;
  }

  delete(isr: IIsrFa): void {
    const modalRef = this.modalService.open(IsrFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.isr = isr;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
