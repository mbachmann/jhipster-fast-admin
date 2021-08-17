import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICostUnitFa } from '../cost-unit-fa.model';
import { CostUnitFaService } from '../service/cost-unit-fa.service';
import { CostUnitFaDeleteDialogComponent } from '../delete/cost-unit-fa-delete-dialog.component';

@Component({
  selector: 'fa-cost-unit-fa',
  templateUrl: './cost-unit-fa.component.html',
})
export class CostUnitFaComponent implements OnInit {
  costUnits?: ICostUnitFa[];
  isLoading = false;

  constructor(protected costUnitService: CostUnitFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.costUnitService.query().subscribe(
      (res: HttpResponse<ICostUnitFa[]>) => {
        this.isLoading = false;
        this.costUnits = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICostUnitFa): number {
    return item.id!;
  }

  delete(costUnit: ICostUnitFa): void {
    const modalRef = this.modalService.open(CostUnitFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.costUnit = costUnit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
