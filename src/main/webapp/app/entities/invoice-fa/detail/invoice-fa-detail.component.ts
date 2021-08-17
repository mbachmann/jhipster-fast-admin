import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoiceFa } from '../invoice-fa.model';

@Component({
  selector: 'fa-invoice-fa-detail',
  templateUrl: './invoice-fa-detail.component.html',
})
export class InvoiceFaDetailComponent implements OnInit {
  invoice: IInvoiceFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
