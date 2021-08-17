import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExchangeRate } from '../exchange-rate.model';
import { ExchangeRateService } from '../service/exchange-rate.service';
import { ExchangeRateDeleteDialogComponent } from '../delete/exchange-rate-delete-dialog.component';

@Component({
  selector: 'fa-exchange-rate',
  templateUrl: './exchange-rate.component.html',
})
export class ExchangeRateComponent implements OnInit {
  exchangeRates?: IExchangeRate[];
  isLoading = false;

  constructor(protected exchangeRateService: ExchangeRateService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.exchangeRateService.query().subscribe(
      (res: HttpResponse<IExchangeRate[]>) => {
        this.isLoading = false;
        this.exchangeRates = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IExchangeRate): number {
    return item.id!;
  }

  delete(exchangeRate: IExchangeRate): void {
    const modalRef = this.modalService.open(ExchangeRateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.exchangeRate = exchangeRate;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
