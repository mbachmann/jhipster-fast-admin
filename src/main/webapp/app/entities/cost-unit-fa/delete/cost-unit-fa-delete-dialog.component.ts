import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICostUnitFa } from '../cost-unit-fa.model';
import { CostUnitFaService } from '../service/cost-unit-fa.service';

@Component({
  templateUrl: './cost-unit-fa-delete-dialog.component.html',
})
export class CostUnitFaDeleteDialogComponent {
  costUnit?: ICostUnitFa;

  constructor(protected costUnitService: CostUnitFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.costUnitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
