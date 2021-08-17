import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExchangeRateFa } from '../exchange-rate-fa.model';
import { ExchangeRateFaService } from '../service/exchange-rate-fa.service';

@Component({
  templateUrl: './exchange-rate-fa-delete-dialog.component.html',
})
export class ExchangeRateFaDeleteDialogComponent {
  exchangeRate?: IExchangeRateFa;

  constructor(protected exchangeRateService: ExchangeRateFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exchangeRateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
