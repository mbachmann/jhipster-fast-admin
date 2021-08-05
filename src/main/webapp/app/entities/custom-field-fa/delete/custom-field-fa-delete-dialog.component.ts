import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomFieldFa } from '../custom-field-fa.model';
import { CustomFieldFaService } from '../service/custom-field-fa.service';

@Component({
  templateUrl: './custom-field-fa-delete-dialog.component.html',
})
export class CustomFieldFaDeleteDialogComponent {
  customField?: ICustomFieldFa;

  constructor(protected customFieldService: CustomFieldFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customFieldService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
