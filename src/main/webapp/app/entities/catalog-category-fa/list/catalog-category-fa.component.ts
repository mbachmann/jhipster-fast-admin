import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogCategoryFa } from '../catalog-category-fa.model';
import { CatalogCategoryFaService } from '../service/catalog-category-fa.service';
import { CatalogCategoryFaDeleteDialogComponent } from '../delete/catalog-category-fa-delete-dialog.component';

@Component({
  selector: 'fa-catalog-category-fa',
  templateUrl: './catalog-category-fa.component.html',
})
export class CatalogCategoryFaComponent implements OnInit {
  catalogCategories?: ICatalogCategoryFa[];
  isLoading = false;

  constructor(protected catalogCategoryService: CatalogCategoryFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.catalogCategoryService.query().subscribe(
      (res: HttpResponse<ICatalogCategoryFa[]>) => {
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

  trackId(index: number, item: ICatalogCategoryFa): number {
    return item.id!;
  }

  delete(catalogCategory: ICatalogCategoryFa): void {
    const modalRef = this.modalService.open(CatalogCategoryFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.catalogCategory = catalogCategory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
