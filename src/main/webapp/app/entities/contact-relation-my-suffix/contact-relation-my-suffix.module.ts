import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactRelationMySuffixComponent } from './list/contact-relation-my-suffix.component';
import { ContactRelationMySuffixDetailComponent } from './detail/contact-relation-my-suffix-detail.component';
import { ContactRelationMySuffixUpdateComponent } from './update/contact-relation-my-suffix-update.component';
import { ContactRelationMySuffixDeleteDialogComponent } from './delete/contact-relation-my-suffix-delete-dialog.component';
import { ContactRelationMySuffixRoutingModule } from './route/contact-relation-my-suffix-routing.module';

@NgModule({
  imports: [SharedModule, ContactRelationMySuffixRoutingModule],
  declarations: [
    ContactRelationMySuffixComponent,
    ContactRelationMySuffixDetailComponent,
    ContactRelationMySuffixUpdateComponent,
    ContactRelationMySuffixDeleteDialogComponent,
  ],
  entryComponents: [ContactRelationMySuffixDeleteDialogComponent],
})
export class ContactRelationMySuffixModule {}
