import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogService } from '../catalog-service.model';
import { CatalogServiceService } from '../service/catalog-service.service';

@Component({
  templateUrl: './catalog-service-delete-dialog.component.html',
})
export class CatalogServiceDeleteDialogComponent {
  catalogService?: ICatalogService;

  constructor(protected catalogServiceService: CatalogServiceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogServiceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
