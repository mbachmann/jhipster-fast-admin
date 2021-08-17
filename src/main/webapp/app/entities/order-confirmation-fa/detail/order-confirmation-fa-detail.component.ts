import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderConfirmationFa } from '../order-confirmation-fa.model';

@Component({
  selector: 'fa-order-confirmation-fa-detail',
  templateUrl: './order-confirmation-fa-detail.component.html',
})
export class OrderConfirmationFaDetailComponent implements OnInit {
  orderConfirmation: IOrderConfirmationFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderConfirmation }) => {
      this.orderConfirmation = orderConfirmation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
