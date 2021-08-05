import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactAddressMySuffixComponent } from './list/contact-address-my-suffix.component';
import { ContactAddressMySuffixDetailComponent } from './detail/contact-address-my-suffix-detail.component';
import { ContactAddressMySuffixUpdateComponent } from './update/contact-address-my-suffix-update.component';
import { ContactAddressMySuffixDeleteDialogComponent } from './delete/contact-address-my-suffix-delete-dialog.component';
import { ContactAddressMySuffixRoutingModule } from './route/contact-address-my-suffix-routing.module';

@NgModule({
  imports: [SharedModule, ContactAddressMySuffixRoutingModule],
  declarations: [
    ContactAddressMySuffixComponent,
    ContactAddressMySuffixDetailComponent,
    ContactAddressMySuffixUpdateComponent,
    ContactAddressMySuffixDeleteDialogComponent,
  ],
  entryComponents: [ContactAddressMySuffixDeleteDialogComponent],
})
export class ContactAddressMySuffixModule {}
