import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEffort } from '../effort.model';
import { EffortService } from '../service/effort.service';

@Component({
  templateUrl: './effort-delete-dialog.component.html',
})
export class EffortDeleteDialogComponent {
  effort?: IEffort;

  constructor(protected effortService: EffortService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.effortService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
