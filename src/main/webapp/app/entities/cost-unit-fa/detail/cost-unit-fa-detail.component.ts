import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICostUnitFa } from '../cost-unit-fa.model';

@Component({
  selector: 'fa-cost-unit-fa-detail',
  templateUrl: './cost-unit-fa-detail.component.html',
})
export class CostUnitFaDetailComponent implements OnInit {
  costUnit: ICostUnitFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ costUnit }) => {
      this.costUnit = costUnit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
