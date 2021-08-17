import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICostUnit } from '../cost-unit.model';
import { CostUnitService } from '../service/cost-unit.service';
import { CostUnitDeleteDialogComponent } from '../delete/cost-unit-delete-dialog.component';

@Component({
  selector: 'fa-cost-unit',
  templateUrl: './cost-unit.component.html',
})
export class CostUnitComponent implements OnInit {
  costUnits?: ICostUnit[];
  isLoading = false;

  constructor(protected costUnitService: CostUnitService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.costUnitService.query().subscribe(
      (res: HttpResponse<ICostUnit[]>) => {
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

  trackId(index: number, item: ICostUnit): number {
    return item.id!;
  }

  delete(costUnit: ICostUnit): void {
    const modalRef = this.modalService.open(CostUnitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.costUnit = costUnit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
