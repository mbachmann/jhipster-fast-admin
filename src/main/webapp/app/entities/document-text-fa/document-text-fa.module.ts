import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentTextFaComponent } from './list/document-text-fa.component';
import { DocumentTextFaDetailComponent } from './detail/document-text-fa-detail.component';
import { DocumentTextFaUpdateComponent } from './update/document-text-fa-update.component';
import { DocumentTextFaDeleteDialogComponent } from './delete/document-text-fa-delete-dialog.component';
import { DocumentTextFaRoutingModule } from './route/document-text-fa-routing.module';

@NgModule({
  imports: [SharedModule, DocumentTextFaRoutingModule],
  declarations: [
    DocumentTextFaComponent,
    DocumentTextFaDetailComponent,
    DocumentTextFaUpdateComponent,
    DocumentTextFaDeleteDialogComponent,
  ],
  entryComponents: [DocumentTextFaDeleteDialogComponent],
})
export class DocumentTextFaModule {}
