import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogCategory } from '../catalog-category.model';
import { CatalogCategoryService } from '../service/catalog-category.service';
import { CatalogCategoryDeleteDialogComponent } from '../delete/catalog-category-delete-dialog.component';

@Component({
  selector: 'fa-catalog-category',
  templateUrl: './catalog-category.component.html',
})
export class CatalogCategoryComponent implements OnInit {
  catalogCategories?: ICatalogCategory[];
  isLoading = false;

  constructor(protected catalogCategoryService: CatalogCategoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.catalogCategoryService.query().subscribe(
      (res: HttpResponse<ICatalogCategory[]>) => {
        this.isLoading = false;
        this.catalogCategories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICatalogCategory): number {
    return item.id!;
  }

  delete(catalogCategory: ICatalogCategory): void {
    const modalRef = this.modalService.open(CatalogCategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.catalogCategory = catalogCategory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
