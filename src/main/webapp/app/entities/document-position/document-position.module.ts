import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentPositionComponent } from './list/document-position.component';
import { DocumentPositionDetailComponent } from './detail/document-position-detail.component';
import { DocumentPositionUpdateComponent } from './update/document-position-update.component';
import { DocumentPositionDeleteDialogComponent } from './delete/document-position-delete-dialog.component';
import { DocumentPositionRoutingModule } from './route/document-position-routing.module';

@NgModule({
  imports: [SharedModule, DocumentPositionRoutingModule],
  declarations: [
    DocumentPositionComponent,
    DocumentPositionDetailComponent,
    DocumentPositionUpdateComponent,
    DocumentPositionDeleteDialogComponent,
  ],
  entryComponents: [DocumentPositionDeleteDialogComponent],
})
export class DocumentPositionModule {}
