import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DescriptiveDocumentTextFaComponent } from './list/descriptive-document-text-fa.component';
import { DescriptiveDocumentTextFaDetailComponent } from './detail/descriptive-document-text-fa-detail.component';
import { DescriptiveDocumentTextFaUpdateComponent } from './update/descriptive-document-text-fa-update.component';
import { DescriptiveDocumentTextFaDeleteDialogComponent } from './delete/descriptive-document-text-fa-delete-dialog.component';
import { DescriptiveDocumentTextFaRoutingModule } from './route/descriptive-document-text-fa-routing.module';

@NgModule({
  imports: [SharedModule, DescriptiveDocumentTextFaRoutingModule],
  declarations: [
    DescriptiveDocumentTextFaComponent,
    DescriptiveDocumentTextFaDetailComponent,
    DescriptiveDocumentTextFaUpdateComponent,
    DescriptiveDocumentTextFaDeleteDialogComponent,
  ],
  entryComponents: [DescriptiveDocumentTextFaDeleteDialogComponent],
})
export class DescriptiveDocumentTextFaModule {}
