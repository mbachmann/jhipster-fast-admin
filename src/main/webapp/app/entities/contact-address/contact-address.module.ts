import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactAddressComponent } from './list/contact-address.component';
import { ContactAddressDetailComponent } from './detail/contact-address-detail.component';
import { ContactAddressUpdateComponent } from './update/contact-address-update.component';
import { ContactAddressDeleteDialogComponent } from './delete/contact-address-delete-dialog.component';
import { ContactAddressRoutingModule } from './route/contact-address-routing.module';

@NgModule({
  imports: [SharedModule, ContactAddressRoutingModule],
  declarations: [
    ContactAddressComponent,
    ContactAddressDetailComponent,
    ContactAddressUpdateComponent,
    ContactAddressDeleteDialogComponent,
  ],
  entryComponents: [ContactAddressDeleteDialogComponent],
})
export class ContactAddressModule {}
