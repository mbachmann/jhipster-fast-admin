import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogUnit } from '../catalog-unit.model';
import { CatalogUnitService } from '../service/catalog-unit.service';

@Component({
  templateUrl: './catalog-unit-delete-dialog.component.html',
})
export class CatalogUnitDeleteDialogComponent {
  catalogUnit?: ICatalogUnit;

  constructor(protected catalogUnitService: CatalogUnitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogUnitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
