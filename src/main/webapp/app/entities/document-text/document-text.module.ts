import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentTextComponent } from './list/document-text.component';
import { DocumentTextDetailComponent } from './detail/document-text-detail.component';
import { DocumentTextUpdateComponent } from './update/document-text-update.component';
import { DocumentTextDeleteDialogComponent } from './delete/document-text-delete-dialog.component';
import { DocumentTextRoutingModule } from './route/document-text-routing.module';

@NgModule({
  imports: [SharedModule, DocumentTextRoutingModule],
  declarations: [DocumentTextComponent, DocumentTextDetailComponent, DocumentTextUpdateComponent, DocumentTextDeleteDialogComponent],
  entryComponents: [DocumentTextDeleteDialogComponent],
})
export class DocumentTextModule {}
