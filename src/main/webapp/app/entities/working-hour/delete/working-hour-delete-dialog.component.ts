import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkingHour } from '../working-hour.model';
import { WorkingHourService } from '../service/working-hour.service';

@Component({
  templateUrl: './working-hour-delete-dialog.component.html',
})
export class WorkingHourDeleteDialogComponent {
  workingHour?: IWorkingHour;

  constructor(protected workingHourService: WorkingHourService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workingHourService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
