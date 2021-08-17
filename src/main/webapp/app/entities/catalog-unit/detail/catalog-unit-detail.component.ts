import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICatalogUnit } from '../catalog-unit.model';

@Component({
  selector: 'fa-catalog-unit-detail',
  templateUrl: './catalog-unit-detail.component.html',
})
export class CatalogUnitDetailComponent implements OnInit {
  catalogUnit: ICatalogUnit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogUnit }) => {
      this.catalogUnit = catalogUnit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
