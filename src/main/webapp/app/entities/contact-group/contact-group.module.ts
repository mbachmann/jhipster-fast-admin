import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactGroupComponent } from './list/contact-group.component';
import { ContactGroupDetailComponent } from './detail/contact-group-detail.component';
import { ContactGroupUpdateComponent } from './update/contact-group-update.component';
import { ContactGroupDeleteDialogComponent } from './delete/contact-group-delete-dialog.component';
import { ContactGroupRoutingModule } from './route/contact-group-routing.module';

@NgModule({
  imports: [SharedModule, ContactGroupRoutingModule],
  declarations: [ContactGroupComponent, ContactGroupDetailComponent, ContactGroupUpdateComponent, ContactGroupDeleteDialogComponent],
  entryComponents: [ContactGroupDeleteDialogComponent],
})
export class ContactGroupModule {}
