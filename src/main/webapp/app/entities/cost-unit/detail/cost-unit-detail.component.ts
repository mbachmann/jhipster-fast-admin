import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICostUnit } from '../cost-unit.model';

@Component({
  selector: 'fa-cost-unit-detail',
  templateUrl: './cost-unit-detail.component.html',
})
export class CostUnitDetailComponent implements OnInit {
  costUnit: ICostUnit | null = null;

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
