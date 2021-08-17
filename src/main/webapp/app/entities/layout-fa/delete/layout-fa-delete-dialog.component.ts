import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILayoutFa } from '../layout-fa.model';
import { LayoutFaService } from '../service/layout-fa.service';

@Component({
  templateUrl: './layout-fa-delete-dialog.component.html',
})
export class LayoutFaDeleteDialogComponent {
  layout?: ILayoutFa;

  constructor(protected layoutService: LayoutFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.layoutService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
