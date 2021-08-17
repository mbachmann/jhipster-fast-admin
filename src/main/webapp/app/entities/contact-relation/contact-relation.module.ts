import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactRelationComponent } from './list/contact-relation.component';
import { ContactRelationDetailComponent } from './detail/contact-relation-detail.component';
import { ContactRelationUpdateComponent } from './update/contact-relation-update.component';
import { ContactRelationDeleteDialogComponent } from './delete/contact-relation-delete-dialog.component';
import { ContactRelationRoutingModule } from './route/contact-relation-routing.module';

@NgModule({
  imports: [SharedModule, ContactRelationRoutingModule],
  declarations: [
    ContactRelationComponent,
    ContactRelationDetailComponent,
    ContactRelationUpdateComponent,
    ContactRelationDeleteDialogComponent,
  ],
  entryComponents: [ContactRelationDeleteDialogComponent],
})
export class ContactRelationModule {}
