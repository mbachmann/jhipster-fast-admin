import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDescriptiveDocumentText } from '../descriptive-document-text.model';
import { DescriptiveDocumentTextService } from '../service/descriptive-document-text.service';

@Component({
  templateUrl: './descriptive-document-text-delete-dialog.component.html',
})
export class DescriptiveDocumentTextDeleteDialogComponent {
  descriptiveDocumentText?: IDescriptiveDocumentText;

  constructor(protected descriptiveDocumentTextService: DescriptiveDocumentTextService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.descriptiveDocumentTextService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
