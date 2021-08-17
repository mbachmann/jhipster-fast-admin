import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactReminder } from '../contact-reminder.model';
import { ContactReminderService } from '../service/contact-reminder.service';

@Component({
  templateUrl: './contact-reminder-delete-dialog.component.html',
})
export class ContactReminderDeleteDialogComponent {
  contactReminder?: IContactReminder;

  constructor(protected contactReminderService: ContactReminderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactReminderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
