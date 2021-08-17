import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICatalogUnitFa } from '../catalog-unit-fa.model';

@Component({
  selector: 'fa-catalog-unit-fa-detail',
  templateUrl: './catalog-unit-fa-detail.component.html',
})
export class CatalogUnitFaDetailComponent implements OnInit {
  catalogUnit: ICatalogUnitFa | null = null;

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
