import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICatalogCategory } from '../catalog-category.model';

@Component({
  selector: 'fa-catalog-category-detail',
  templateUrl: './catalog-category-detail.component.html',
})
export class CatalogCategoryDetailComponent implements OnInit {
  catalogCategory: ICatalogCategory | null = null;

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
