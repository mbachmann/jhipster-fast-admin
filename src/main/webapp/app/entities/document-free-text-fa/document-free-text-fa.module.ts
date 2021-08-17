import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentFreeTextFaComponent } from './list/document-free-text-fa.component';
import { DocumentFreeTextFaDetailComponent } from './detail/document-free-text-fa-detail.component';
import { DocumentFreeTextFaUpdateComponent } from './update/document-free-text-fa-update.component';
import { DocumentFreeTextFaDeleteDialogComponent } from './delete/document-free-text-fa-delete-dialog.component';
import { DocumentFreeTextFaRoutingModule } from './route/document-free-text-fa-routing.module';

@NgModule({
  imports: [SharedModule, DocumentFreeTextFaRoutingModule],
  declarations: [
    DocumentFreeTextFaComponent,
    DocumentFreeTextFaDetailComponent,
    DocumentFreeTextFaUpdateComponent,
    DocumentFreeTextFaDeleteDialogComponent,
  ],
  entryComponents: [DocumentFreeTextFaDeleteDialogComponent],
})
export class DocumentFreeTextFaModule {}
