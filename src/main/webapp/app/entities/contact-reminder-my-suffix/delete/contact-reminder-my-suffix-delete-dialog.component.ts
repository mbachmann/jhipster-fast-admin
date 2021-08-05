import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactReminderMySuffix } from '../contact-reminder-my-suffix.model';
import { ContactReminderMySuffixService } from '../service/contact-reminder-my-suffix.service';

@Component({
  templateUrl: './contact-reminder-my-suffix-delete-dialog.component.html',
})
export class ContactReminderMySuffixDeleteDialogComponent {
  contactReminder?: IContactReminderMySuffix;

  constructor(protected contactReminderService: ContactReminderMySuffixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactReminderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
