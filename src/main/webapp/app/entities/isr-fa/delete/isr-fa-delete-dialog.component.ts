import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIsrFa } from '../isr-fa.model';
import { IsrFaService } from '../service/isr-fa.service';

@Component({
  templateUrl: './isr-fa-delete-dialog.component.html',
})
export class IsrFaDeleteDialogComponent {
  isr?: IIsrFa;

  constructor(protected isrService: IsrFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.isrService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
