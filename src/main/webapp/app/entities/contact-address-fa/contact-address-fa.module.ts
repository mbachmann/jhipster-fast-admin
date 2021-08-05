import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactAddressFaComponent } from './list/contact-address-fa.component';
import { ContactAddressFaDetailComponent } from './detail/contact-address-fa-detail.component';
import { ContactAddressFaUpdateComponent } from './update/contact-address-fa-update.component';
import { ContactAddressFaDeleteDialogComponent } from './delete/contact-address-fa-delete-dialog.component';
import { ContactAddressFaRoutingModule } from './route/contact-address-fa-routing.module';

@NgModule({
  imports: [SharedModule, ContactAddressFaRoutingModule],
  declarations: [
    ContactAddressFaComponent,
    ContactAddressFaDetailComponent,
    ContactAddressFaUpdateComponent,
    ContactAddressFaDeleteDialogComponent,
  ],
  entryComponents: [ContactAddressFaDeleteDialogComponent],
})
export class ContactAddressFaModule {}
