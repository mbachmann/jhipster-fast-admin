import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactReminderFaComponent } from './list/contact-reminder-fa.component';
import { ContactReminderFaDetailComponent } from './detail/contact-reminder-fa-detail.component';
import { ContactReminderFaUpdateComponent } from './update/contact-reminder-fa-update.component';
import { ContactReminderFaDeleteDialogComponent } from './delete/contact-reminder-fa-delete-dialog.component';
import { ContactReminderFaRoutingModule } from './route/contact-reminder-fa-routing.module';

@NgModule({
  imports: [SharedModule, ContactReminderFaRoutingModule],
  declarations: [
    ContactReminderFaComponent,
    ContactReminderFaDetailComponent,
    ContactReminderFaUpdateComponent,
    ContactReminderFaDeleteDialogComponent,
  ],
  entryComponents: [ContactReminderFaDeleteDialogComponent],
})
export class ContactReminderFaModule {}
