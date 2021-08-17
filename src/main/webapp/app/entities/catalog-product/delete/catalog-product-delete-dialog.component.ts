import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogProduct } from '../catalog-product.model';
import { CatalogProductService } from '../service/catalog-product.service';

@Component({
  templateUrl: './catalog-product-delete-dialog.component.html',
})
export class CatalogProductDeleteDialogComponent {
  catalogProduct?: ICatalogProduct;

  constructor(protected catalogProductService: CatalogProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogProductService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
