import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICatalogServiceFa } from '../catalog-service-fa.model';

@Component({
  selector: 'fa-catalog-service-fa-detail',
  templateUrl: './catalog-service-fa-detail.component.html',
})
export class CatalogServiceFaDetailComponent implements OnInit {
  catalogService: ICatalogServiceFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogService }) => {
      this.catalogService = catalogService;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
