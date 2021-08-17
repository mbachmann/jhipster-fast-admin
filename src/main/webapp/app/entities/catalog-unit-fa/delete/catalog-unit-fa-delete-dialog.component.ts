import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogUnitFa } from '../catalog-unit-fa.model';
import { CatalogUnitFaService } from '../service/catalog-unit-fa.service';

@Component({
  templateUrl: './catalog-unit-fa-delete-dialog.component.html',
})
export class CatalogUnitFaDeleteDialogComponent {
  catalogUnit?: ICatalogUnitFa;

  constructor(protected catalogUnitService: CatalogUnitFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogUnitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
