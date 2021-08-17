import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogServiceFa } from '../catalog-service-fa.model';
import { CatalogServiceFaService } from '../service/catalog-service-fa.service';

@Component({
  templateUrl: './catalog-service-fa-delete-dialog.component.html',
})
export class CatalogServiceFaDeleteDialogComponent {
  catalogService?: ICatalogServiceFa;

  constructor(protected catalogServiceService: CatalogServiceFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogServiceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
