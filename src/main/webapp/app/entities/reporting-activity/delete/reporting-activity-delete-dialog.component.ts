import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReportingActivity } from '../reporting-activity.model';
import { ReportingActivityService } from '../service/reporting-activity.service';

@Component({
  templateUrl: './reporting-activity-delete-dialog.component.html',
})
export class ReportingActivityDeleteDialogComponent {
  reportingActivity?: IReportingActivity;

  constructor(protected reportingActivityService: ReportingActivityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reportingActivityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
