import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExchangeRateFa } from '../exchange-rate-fa.model';

@Component({
  selector: 'fa-exchange-rate-fa-detail',
  templateUrl: './exchange-rate-fa-detail.component.html',
})
export class ExchangeRateFaDetailComponent implements OnInit {
  exchangeRate: IExchangeRateFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exchangeRate }) => {
      this.exchangeRate = exchangeRate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
