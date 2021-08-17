import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactAccountFaComponent } from './list/contact-account-fa.component';
import { ContactAccountFaDetailComponent } from './detail/contact-account-fa-detail.component';
import { ContactAccountFaUpdateComponent } from './update/contact-account-fa-update.component';
import { ContactAccountFaDeleteDialogComponent } from './delete/contact-account-fa-delete-dialog.component';
import { ContactAccountFaRoutingModule } from './route/contact-account-fa-routing.module';

@NgModule({
  imports: [SharedModule, ContactAccountFaRoutingModule],
  declarations: [
    ContactAccountFaComponent,
    ContactAccountFaDetailComponent,
    ContactAccountFaUpdateComponent,
    ContactAccountFaDeleteDialogComponent,
  ],
  entryComponents: [ContactAccountFaDeleteDialogComponent],
})
export class ContactAccountFaModule {}
