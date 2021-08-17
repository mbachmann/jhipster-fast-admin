import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILayout } from '../layout.model';
import { LayoutService } from '../service/layout.service';
import { LayoutDeleteDialogComponent } from '../delete/layout-delete-dialog.component';

@Component({
  selector: 'fa-layout',
  templateUrl: './layout.component.html',
})
export class LayoutComponent implements OnInit {
  layouts?: ILayout[];
  isLoading = false;

  constructor(protected layoutService: LayoutService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.layoutService.query().subscribe(
      (res: HttpResponse<ILayout[]>) => {
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

  trackId(index: number, item: ILayout): number {
    return item.id!;
  }

  delete(layout: ILayout): void {
    const modalRef = this.modalService.open(LayoutDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.layout = layout;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
