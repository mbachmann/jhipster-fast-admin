import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentLetterComponent } from './list/document-letter.component';
import { DocumentLetterDetailComponent } from './detail/document-letter-detail.component';
import { DocumentLetterUpdateComponent } from './update/document-letter-update.component';
import { DocumentLetterDeleteDialogComponent } from './delete/document-letter-delete-dialog.component';
import { DocumentLetterRoutingModule } from './route/document-letter-routing.module';

@NgModule({
  imports: [SharedModule, DocumentLetterRoutingModule],
  declarations: [
    DocumentLetterComponent,
    DocumentLetterDetailComponent,
    DocumentLetterUpdateComponent,
    DocumentLetterDeleteDialogComponent,
  ],
  entryComponents: [DocumentLetterDeleteDialogComponent],
})
export class DocumentLetterModule {}
