import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentLetterFaComponent } from './list/document-letter-fa.component';
import { DocumentLetterFaDetailComponent } from './detail/document-letter-fa-detail.component';
import { DocumentLetterFaUpdateComponent } from './update/document-letter-fa-update.component';
import { DocumentLetterFaDeleteDialogComponent } from './delete/document-letter-fa-delete-dialog.component';
import { DocumentLetterFaRoutingModule } from './route/document-letter-fa-routing.module';

@NgModule({
  imports: [SharedModule, DocumentLetterFaRoutingModule],
  declarations: [
    DocumentLetterFaComponent,
    DocumentLetterFaDetailComponent,
    DocumentLetterFaUpdateComponent,
    DocumentLetterFaDeleteDialogComponent,
  ],
  entryComponents: [DocumentLetterFaDeleteDialogComponent],
})
export class DocumentLetterFaModule {}
