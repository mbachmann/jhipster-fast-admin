import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactReminderComponent } from './list/contact-reminder.component';
import { ContactReminderDetailComponent } from './detail/contact-reminder-detail.component';
import { ContactReminderUpdateComponent } from './update/contact-reminder-update.component';
import { ContactReminderDeleteDialogComponent } from './delete/contact-reminder-delete-dialog.component';
import { ContactReminderRoutingModule } from './route/contact-reminder-routing.module';

@NgModule({
  imports: [SharedModule, ContactReminderRoutingModule],
  declarations: [
    ContactReminderComponent,
    ContactReminderDetailComponent,
    ContactReminderUpdateComponent,
    ContactReminderDeleteDialogComponent,
  ],
  entryComponents: [ContactReminderDeleteDialogComponent],
})
export class ContactReminderModule {}
