import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IValueAddedTax } from '../value-added-tax.model';
import { ValueAddedTaxService } from '../service/value-added-tax.service';

@Component({
  templateUrl: './value-added-tax-delete-dialog.component.html',
})
export class ValueAddedTaxDeleteDialogComponent {
  valueAddedTax?: IValueAddedTax;

  constructor(protected valueAddedTaxService: ValueAddedTaxService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.valueAddedTaxService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
