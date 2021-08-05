import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactReminderFa } from '../contact-reminder-fa.model';
import { ContactReminderFaService } from '../service/contact-reminder-fa.service';

@Component({
  templateUrl: './contact-reminder-fa-delete-dialog.component.html',
})
export class ContactReminderFaDeleteDialogComponent {
  contactReminder?: IContactReminderFa;

  constructor(protected contactReminderService: ContactReminderFaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactReminderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
