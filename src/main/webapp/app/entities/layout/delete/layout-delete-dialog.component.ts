import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILayout } from '../layout.model';
import { LayoutService } from '../service/layout.service';

@Component({
  templateUrl: './layout-delete-dialog.component.html',
})
export class LayoutDeleteDialogComponent {
  layout?: ILayout;

  constructor(protected layoutService: LayoutService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.layoutService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
