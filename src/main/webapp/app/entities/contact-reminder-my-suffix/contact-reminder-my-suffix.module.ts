import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactReminderMySuffixComponent } from './list/contact-reminder-my-suffix.component';
import { ContactReminderMySuffixDetailComponent } from './detail/contact-reminder-my-suffix-detail.component';
import { ContactReminderMySuffixUpdateComponent } from './update/contact-reminder-my-suffix-update.component';
import { ContactReminderMySuffixDeleteDialogComponent } from './delete/contact-reminder-my-suffix-delete-dialog.component';
import { ContactReminderMySuffixRoutingModule } from './route/contact-reminder-my-suffix-routing.module';

@NgModule({
  imports: [SharedModule, ContactReminderMySuffixRoutingModule],
  declarations: [
    ContactReminderMySuffixComponent,
    ContactReminderMySuffixDetailComponent,
    ContactReminderMySuffixUpdateComponent,
    ContactReminderMySuffixDeleteDialogComponent,
  ],
  entryComponents: [ContactReminderMySuffixDeleteDialogComponent],
})
export class ContactReminderMySuffixModule {}
