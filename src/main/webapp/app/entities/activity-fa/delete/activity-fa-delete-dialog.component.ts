import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IActivityFa } from '../activity-fa.model';
import { ActivityFaService } from '../service/activity-fa.service';

@Component({
  templateUrl: './activity-fa-delete-dialog.component.html',
})
export class ActivityFaDeleteDialogComponent {
  activity?: IActivityFa;

  constructor(protected activityService: ActivityFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.activityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
