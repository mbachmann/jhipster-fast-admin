import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICatalogCategoryFa } from '../catalog-category-fa.model';

@Component({
  selector: 'fa-catalog-category-fa-detail',
  templateUrl: './catalog-category-fa-detail.component.html',
})
export class CatalogCategoryFaDetailComponent implements OnInit {
  catalogCategory: ICatalogCategoryFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogCategory }) => {
      this.catalogCategory = catalogCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
