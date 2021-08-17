import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DescriptiveDocumentTextComponent } from './list/descriptive-document-text.component';
import { DescriptiveDocumentTextDetailComponent } from './detail/descriptive-document-text-detail.component';
import { DescriptiveDocumentTextUpdateComponent } from './update/descriptive-document-text-update.component';
import { DescriptiveDocumentTextDeleteDialogComponent } from './delete/descriptive-document-text-delete-dialog.component';
import { DescriptiveDocumentTextRoutingModule } from './route/descriptive-document-text-routing.module';

@NgModule({
  imports: [SharedModule, DescriptiveDocumentTextRoutingModule],
  declarations: [
    DescriptiveDocumentTextComponent,
    DescriptiveDocumentTextDetailComponent,
    DescriptiveDocumentTextUpdateComponent,
    DescriptiveDocumentTextDeleteDialogComponent,
  ],
  entryComponents: [DescriptiveDocumentTextDeleteDialogComponent],
})
export class DescriptiveDocumentTextModule {}
