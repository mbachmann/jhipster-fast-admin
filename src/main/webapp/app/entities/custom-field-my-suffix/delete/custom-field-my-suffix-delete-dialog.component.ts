import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomFieldMySuffix } from '../custom-field-my-suffix.model';
import { CustomFieldMySuffixService } from '../service/custom-field-my-suffix.service';

@Component({
  templateUrl: './custom-field-my-suffix-delete-dialog.component.html',
})
export class CustomFieldMySuffixDeleteDialogComponent {
  customField?: ICustomFieldMySuffix;

  constructor(protected customFieldService: CustomFieldMySuffixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customFieldService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
