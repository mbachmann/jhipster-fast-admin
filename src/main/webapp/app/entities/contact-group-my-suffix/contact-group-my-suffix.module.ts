import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactGroupMySuffixComponent } from './list/contact-group-my-suffix.component';
import { ContactGroupMySuffixDetailComponent } from './detail/contact-group-my-suffix-detail.component';
import { ContactGroupMySuffixUpdateComponent } from './update/contact-group-my-suffix-update.component';
import { ContactGroupMySuffixDeleteDialogComponent } from './delete/contact-group-my-suffix-delete-dialog.component';
import { ContactGroupMySuffixRoutingModule } from './route/contact-group-my-suffix-routing.module';

@NgModule({
  imports: [SharedModule, ContactGroupMySuffixRoutingModule],
  declarations: [
    ContactGroupMySuffixComponent,
    ContactGroupMySuffixDetailComponent,
    ContactGroupMySuffixUpdateComponent,
    ContactGroupMySuffixDeleteDialogComponent,
  ],
  entryComponents: [ContactGroupMySuffixDeleteDialogComponent],
})
export class ContactGroupMySuffixModule {}
