import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExchangeRateFa } from '../exchange-rate-fa.model';
import { ExchangeRateFaService } from '../service/exchange-rate-fa.service';
import { ExchangeRateFaDeleteDialogComponent } from '../delete/exchange-rate-fa-delete-dialog.component';

@Component({
  selector: 'fa-exchange-rate-fa',
  templateUrl: './exchange-rate-fa.component.html',
})
export class ExchangeRateFaComponent implements OnInit {
  exchangeRates?: IExchangeRateFa[];
  isLoading = false;

  constructor(protected exchangeRateService: ExchangeRateFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.exchangeRateService.query().subscribe(
      (res: HttpResponse<IExchangeRateFa[]>) => {
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

  trackId(index: number, item: IExchangeRateFa): number {
    return item.id!;
  }

  delete(exchangeRate: IExchangeRateFa): void {
    const modalRef = this.modalService.open(ExchangeRateFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.exchangeRate = exchangeRate;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
