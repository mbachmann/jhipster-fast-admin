import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactGroupFaComponent } from './list/contact-group-fa.component';
import { ContactGroupFaDetailComponent } from './detail/contact-group-fa-detail.component';
import { ContactGroupFaUpdateComponent } from './update/contact-group-fa-update.component';
import { ContactGroupFaDeleteDialogComponent } from './delete/contact-group-fa-delete-dialog.component';
import { ContactGroupFaRoutingModule } from './route/contact-group-fa-routing.module';

@NgModule({
  imports: [SharedModule, ContactGroupFaRoutingModule],
  declarations: [
    ContactGroupFaComponent,
    ContactGroupFaDetailComponent,
    ContactGroupFaUpdateComponent,
    ContactGroupFaDeleteDialogComponent,
  ],
  entryComponents: [ContactGroupFaDeleteDialogComponent],
})
export class ContactGroupFaModule {}
