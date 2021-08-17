import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICatalogProductFa } from '../catalog-product-fa.model';

@Component({
  selector: 'fa-catalog-product-fa-detail',
  templateUrl: './catalog-product-fa-detail.component.html',
})
export class CatalogProductFaDetailComponent implements OnInit {
  catalogProduct: ICatalogProductFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogProduct }) => {
      this.catalogProduct = catalogProduct;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
