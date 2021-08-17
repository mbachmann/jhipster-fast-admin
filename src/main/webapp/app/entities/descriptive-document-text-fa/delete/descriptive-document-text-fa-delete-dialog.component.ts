import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDescriptiveDocumentTextFa } from '../descriptive-document-text-fa.model';
import { DescriptiveDocumentTextFaService } from '../service/descriptive-document-text-fa.service';

@Component({
  templateUrl: './descriptive-document-text-fa-delete-dialog.component.html',
})
export class DescriptiveDocumentTextFaDeleteDialogComponent {
  descriptiveDocumentText?: IDescriptiveDocumentTextFa;

  constructor(protected descriptiveDocumentTextService: DescriptiveDocumentTextFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.descriptiveDocumentTextService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
