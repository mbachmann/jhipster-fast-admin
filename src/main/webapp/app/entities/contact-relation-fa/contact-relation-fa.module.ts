import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactRelationFaComponent } from './list/contact-relation-fa.component';
import { ContactRelationFaDetailComponent } from './detail/contact-relation-fa-detail.component';
import { ContactRelationFaUpdateComponent } from './update/contact-relation-fa-update.component';
import { ContactRelationFaDeleteDialogComponent } from './delete/contact-relation-fa-delete-dialog.component';
import { ContactRelationFaRoutingModule } from './route/contact-relation-fa-routing.module';

@NgModule({
  imports: [SharedModule, ContactRelationFaRoutingModule],
  declarations: [
    ContactRelationFaComponent,
    ContactRelationFaDetailComponent,
    ContactRelationFaUpdateComponent,
    ContactRelationFaDeleteDialogComponent,
  ],
  entryComponents: [ContactRelationFaDeleteDialogComponent],
})
export class ContactRelationFaModule {}
