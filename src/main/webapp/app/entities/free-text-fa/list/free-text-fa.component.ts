import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFreeTextFa } from '../free-text-fa.model';
import { FreeTextFaService } from '../service/free-text-fa.service';
import { FreeTextFaDeleteDialogComponent } from '../delete/free-text-fa-delete-dialog.component';

@Component({
  selector: 'fa-free-text-fa',
  templateUrl: './free-text-fa.component.html',
})
export class FreeTextFaComponent implements OnInit {
  freeTexts?: IFreeTextFa[];
  isLoading = false;

  constructor(protected freeTextService: FreeTextFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.freeTextService.query().subscribe(
      (res: HttpResponse<IFreeTextFa[]>) => {
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

  trackId(index: number, item: IFreeTextFa): number {
    return item.id!;
  }

  delete(freeText: IFreeTextFa): void {
    const modalRef = this.modalService.open(FreeTextFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.freeText = freeText;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
