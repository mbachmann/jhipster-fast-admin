import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderConfirmation } from '../order-confirmation.model';

@Component({
  selector: 'fa-order-confirmation-detail',
  templateUrl: './order-confirmation-detail.component.html',
})
export class OrderConfirmationDetailComponent implements OnInit {
  orderConfirmation: IOrderConfirmation | null = null;

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
