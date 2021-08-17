import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICostUnit } from '../cost-unit.model';
import { CostUnitService } from '../service/cost-unit.service';

@Component({
  templateUrl: './cost-unit-delete-dialog.component.html',
})
export class CostUnitDeleteDialogComponent {
  costUnit?: ICostUnit;

  constructor(protected costUnitService: CostUnitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.costUnitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
