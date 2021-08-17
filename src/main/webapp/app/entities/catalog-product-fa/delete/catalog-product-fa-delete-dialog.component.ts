import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogProductFa } from '../catalog-product-fa.model';
import { CatalogProductFaService } from '../service/catalog-product-fa.service';

@Component({
  templateUrl: './catalog-product-fa-delete-dialog.component.html',
})
export class CatalogProductFaDeleteDialogComponent {
  catalogProduct?: ICatalogProductFa;

  constructor(protected catalogProductService: CatalogProductFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogProductService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
