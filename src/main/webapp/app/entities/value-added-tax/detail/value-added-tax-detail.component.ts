import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IValueAddedTax } from '../value-added-tax.model';

@Component({
  selector: 'fa-value-added-tax-detail',
  templateUrl: './value-added-tax-detail.component.html',
})
export class ValueAddedTaxDetailComponent implements OnInit {
  valueAddedTax: IValueAddedTax | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ valueAddedTax }) => {
      this.valueAddedTax = valueAddedTax;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
