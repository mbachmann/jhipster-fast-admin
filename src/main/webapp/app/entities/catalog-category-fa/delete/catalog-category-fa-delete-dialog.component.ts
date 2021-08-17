import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogCategoryFa } from '../catalog-category-fa.model';
import { CatalogCategoryFaService } from '../service/catalog-category-fa.service';

@Component({
  templateUrl: './catalog-category-fa-delete-dialog.component.html',
})
export class CatalogCategoryFaDeleteDialogComponent {
  catalogCategory?: ICatalogCategoryFa;

  constructor(protected catalogCategoryService: CatalogCategoryFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
