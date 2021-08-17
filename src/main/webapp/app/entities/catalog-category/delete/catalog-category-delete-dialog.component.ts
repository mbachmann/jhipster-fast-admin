import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogCategory } from '../catalog-category.model';
import { CatalogCategoryService } from '../service/catalog-category.service';

@Component({
  templateUrl: './catalog-category-delete-dialog.component.html',
})
export class CatalogCategoryDeleteDialogComponent {
  catalogCategory?: ICatalogCategory;

  constructor(protected catalogCategoryService: CatalogCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
