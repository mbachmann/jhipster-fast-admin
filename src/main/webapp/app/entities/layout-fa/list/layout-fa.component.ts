import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILayoutFa } from '../layout-fa.model';
import { LayoutFaService } from '../service/layout-fa.service';
import { LayoutFaDeleteDialogComponent } from '../delete/layout-fa-delete-dialog.component';

@Component({
  selector: 'fa-layout-fa',
  templateUrl: './layout-fa.component.html',
})
export class LayoutFaComponent implements OnInit {
  layouts?: ILayoutFa[];
  isLoading = false;

  constructor(protected layoutService: LayoutFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.layoutService.query().subscribe(
      (res: HttpResponse<ILayoutFa[]>) => {
        this.isLoading = false;
        this.layouts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILayoutFa): number {
    return item.id!;
  }

  delete(layout: ILayoutFa): void {
    const modalRef = this.modalService.open(LayoutFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.layout = layout;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
