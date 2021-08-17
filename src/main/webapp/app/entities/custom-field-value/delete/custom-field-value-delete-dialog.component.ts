import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomFieldValue } from '../custom-field-value.model';
import { CustomFieldValueService } from '../service/custom-field-value.service';

@Component({
  templateUrl: './custom-field-value-delete-dialog.component.html',
})
export class CustomFieldValueDeleteDialogComponent {
  customFieldValue?: ICustomFieldValue;

  constructor(protected customFieldValueService: CustomFieldValueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customFieldValueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
