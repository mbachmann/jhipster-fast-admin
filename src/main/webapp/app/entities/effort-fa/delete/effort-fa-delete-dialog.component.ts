import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEffortFa } from '../effort-fa.model';
import { EffortFaService } from '../service/effort-fa.service';

@Component({
  templateUrl: './effort-fa-delete-dialog.component.html',
})
export class EffortFaDeleteDialogComponent {
  effort?: IEffortFa;

  constructor(protected effortService: EffortFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.effortService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
