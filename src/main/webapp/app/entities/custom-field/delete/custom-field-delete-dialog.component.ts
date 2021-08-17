import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomField } from '../custom-field.model';
import { CustomFieldService } from '../service/custom-field.service';

@Component({
  templateUrl: './custom-field-delete-dialog.component.html',
})
export class CustomFieldDeleteDialogComponent {
  customField?: ICustomField;

  constructor(protected customFieldService: CustomFieldService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customFieldService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
