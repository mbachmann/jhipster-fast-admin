import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIsr } from '../isr.model';
import { IsrService } from '../service/isr.service';

@Component({
  templateUrl: './isr-delete-dialog.component.html',
})
export class IsrDeleteDialogComponent {
  isr?: IIsr;

  constructor(protected isrService: IsrService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.isrService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
