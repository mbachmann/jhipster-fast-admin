import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactFaComponent } from './list/contact-fa.component';
import { ContactFaDetailComponent } from './detail/contact-fa-detail.component';
import { ContactFaUpdateComponent } from './update/contact-fa-update.component';
import { ContactFaDeleteDialogComponent } from './delete/contact-fa-delete-dialog.component';
import { ContactFaRoutingModule } from './route/contact-fa-routing.module';

@NgModule({
  imports: [SharedModule, ContactFaRoutingModule],
  declarations: [ContactFaComponent, ContactFaDetailComponent, ContactFaUpdateComponent, ContactFaDeleteDialogComponent],
  entryComponents: [ContactFaDeleteDialogComponent],
})
export class ContactFaModule {}
