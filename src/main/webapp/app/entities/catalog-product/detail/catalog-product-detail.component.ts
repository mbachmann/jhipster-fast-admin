import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICatalogProduct } from '../catalog-product.model';

@Component({
  selector: 'fa-catalog-product-detail',
  templateUrl: './catalog-product-detail.component.html',
})
export class CatalogProductDetailComponent implements OnInit {
  catalogProduct: ICatalogProduct | null = null;

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
