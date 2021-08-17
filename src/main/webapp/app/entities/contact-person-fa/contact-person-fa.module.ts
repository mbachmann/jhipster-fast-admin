import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactPersonFaComponent } from './list/contact-person-fa.component';
import { ContactPersonFaDetailComponent } from './detail/contact-person-fa-detail.component';
import { ContactPersonFaUpdateComponent } from './update/contact-person-fa-update.component';
import { ContactPersonFaDeleteDialogComponent } from './delete/contact-person-fa-delete-dialog.component';
import { ContactPersonFaRoutingModule } from './route/contact-person-fa-routing.module';

@NgModule({
  imports: [SharedModule, ContactPersonFaRoutingModule],
  declarations: [
    ContactPersonFaComponent,
    ContactPersonFaDetailComponent,
    ContactPersonFaUpdateComponent,
    ContactPersonFaDeleteDialogComponent,
  ],
  entryComponents: [ContactPersonFaDeleteDialogComponent],
})
export class ContactPersonFaModule {}
