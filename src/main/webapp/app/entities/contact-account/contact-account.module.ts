import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactAccountComponent } from './list/contact-account.component';
import { ContactAccountDetailComponent } from './detail/contact-account-detail.component';
import { ContactAccountUpdateComponent } from './update/contact-account-update.component';
import { ContactAccountDeleteDialogComponent } from './delete/contact-account-delete-dialog.component';
import { ContactAccountRoutingModule } from './route/contact-account-routing.module';

@NgModule({
  imports: [SharedModule, ContactAccountRoutingModule],
  declarations: [
    ContactAccountComponent,
    ContactAccountDetailComponent,
    ContactAccountUpdateComponent,
    ContactAccountDeleteDialogComponent,
  ],
  entryComponents: [ContactAccountDeleteDialogComponent],
})
export class ContactAccountModule {}
