import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentPositionFaComponent } from './list/document-position-fa.component';
import { DocumentPositionFaDetailComponent } from './detail/document-position-fa-detail.component';
import { DocumentPositionFaUpdateComponent } from './update/document-position-fa-update.component';
import { DocumentPositionFaDeleteDialogComponent } from './delete/document-position-fa-delete-dialog.component';
import { DocumentPositionFaRoutingModule } from './route/document-position-fa-routing.module';

@NgModule({
  imports: [SharedModule, DocumentPositionFaRoutingModule],
  declarations: [
    DocumentPositionFaComponent,
    DocumentPositionFaDetailComponent,
    DocumentPositionFaUpdateComponent,
    DocumentPositionFaDeleteDialogComponent,
  ],
  entryComponents: [DocumentPositionFaDeleteDialogComponent],
})
export class DocumentPositionFaModule {}
