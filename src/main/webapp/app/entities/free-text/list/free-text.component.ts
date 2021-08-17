import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFreeText } from '../free-text.model';
import { FreeTextService } from '../service/free-text.service';
import { FreeTextDeleteDialogComponent } from '../delete/free-text-delete-dialog.component';

@Component({
  selector: 'fa-free-text',
  templateUrl: './free-text.component.html',
})
export class FreeTextComponent implements OnInit {
  freeTexts?: IFreeText[];
  isLoading = false;

  constructor(protected freeTextService: FreeTextService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.freeTextService.query().subscribe(
      (res: HttpResponse<IFreeText[]>) => {
        this.isLoading = false;
        this.freeTexts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFreeText): number {
    return item.id!;
  }

  delete(freeText: IFreeText): void {
    const modalRef = this.modalService.open(FreeTextDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.freeText = freeText;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
