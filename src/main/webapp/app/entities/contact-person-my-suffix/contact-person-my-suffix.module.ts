import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactPersonMySuffixComponent } from './list/contact-person-my-suffix.component';
import { ContactPersonMySuffixDetailComponent } from './detail/contact-person-my-suffix-detail.component';
import { ContactPersonMySuffixUpdateComponent } from './update/contact-person-my-suffix-update.component';
import { ContactPersonMySuffixDeleteDialogComponent } from './delete/contact-person-my-suffix-delete-dialog.component';
import { ContactPersonMySuffixRoutingModule } from './route/contact-person-my-suffix-routing.module';

@NgModule({
  imports: [SharedModule, ContactPersonMySuffixRoutingModule],
  declarations: [
    ContactPersonMySuffixComponent,
    ContactPersonMySuffixDetailComponent,
    ContactPersonMySuffixUpdateComponent,
    ContactPersonMySuffixDeleteDialogComponent,
  ],
  entryComponents: [ContactPersonMySuffixDeleteDialogComponent],
})
export class ContactPersonMySuffixModule {}
