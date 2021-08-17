import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentInvoiceWorkflowFaComponent } from './list/document-invoice-workflow-fa.component';
import { DocumentInvoiceWorkflowFaDetailComponent } from './detail/document-invoice-workflow-fa-detail.component';
import { DocumentInvoiceWorkflowFaUpdateComponent } from './update/document-invoice-workflow-fa-update.component';
import { DocumentInvoiceWorkflowFaDeleteDialogComponent } from './delete/document-invoice-workflow-fa-delete-dialog.component';
import { DocumentInvoiceWorkflowFaRoutingModule } from './route/document-invoice-workflow-fa-routing.module';

@NgModule({
  imports: [SharedModule, DocumentInvoiceWorkflowFaRoutingModule],
  declarations: [
    DocumentInvoiceWorkflowFaComponent,
    DocumentInvoiceWorkflowFaDetailComponent,
    DocumentInvoiceWorkflowFaUpdateComponent,
    DocumentInvoiceWorkflowFaDeleteDialogComponent,
  ],
  entryComponents: [DocumentInvoiceWorkflowFaDeleteDialogComponent],
})
export class DocumentInvoiceWorkflowFaModule {}
