import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactMySuffixComponent } from './list/contact-my-suffix.component';
import { ContactMySuffixDetailComponent } from './detail/contact-my-suffix-detail.component';
import { ContactMySuffixUpdateComponent } from './update/contact-my-suffix-update.component';
import { ContactMySuffixDeleteDialogComponent } from './delete/contact-my-suffix-delete-dialog.component';
import { ContactMySuffixRoutingModule } from './route/contact-my-suffix-routing.module';

@NgModule({
  imports: [SharedModule, ContactMySuffixRoutingModule],
  declarations: [
    ContactMySuffixComponent,
    ContactMySuffixDetailComponent,
    ContactMySuffixUpdateComponent,
    ContactMySuffixDeleteDialogComponent,
  ],
  entryComponents: [ContactMySuffixDeleteDialogComponent],
})
export class ContactMySuffixModule {}
