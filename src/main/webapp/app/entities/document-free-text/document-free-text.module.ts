import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentFreeTextComponent } from './list/document-free-text.component';
import { DocumentFreeTextDetailComponent } from './detail/document-free-text-detail.component';
import { DocumentFreeTextUpdateComponent } from './update/document-free-text-update.component';
import { DocumentFreeTextDeleteDialogComponent } from './delete/document-free-text-delete-dialog.component';
import { DocumentFreeTextRoutingModule } from './route/document-free-text-routing.module';

@NgModule({
  imports: [SharedModule, DocumentFreeTextRoutingModule],
  declarations: [
    DocumentFreeTextComponent,
    DocumentFreeTextDetailComponent,
    DocumentFreeTextUpdateComponent,
    DocumentFreeTextDeleteDialogComponent,
  ],
  entryComponents: [DocumentFreeTextDeleteDialogComponent],
})
export class DocumentFreeTextModule {}
