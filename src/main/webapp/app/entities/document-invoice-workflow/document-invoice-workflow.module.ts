import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentInvoiceWorkflowComponent } from './list/document-invoice-workflow.component';
import { DocumentInvoiceWorkflowDetailComponent } from './detail/document-invoice-workflow-detail.component';
import { DocumentInvoiceWorkflowUpdateComponent } from './update/document-invoice-workflow-update.component';
import { DocumentInvoiceWorkflowDeleteDialogComponent } from './delete/document-invoice-workflow-delete-dialog.component';
import { DocumentInvoiceWorkflowRoutingModule } from './route/document-invoice-workflow-routing.module';

@NgModule({
  imports: [SharedModule, DocumentInvoiceWorkflowRoutingModule],
  declarations: [
    DocumentInvoiceWorkflowComponent,
    DocumentInvoiceWorkflowDetailComponent,
    DocumentInvoiceWorkflowUpdateComponent,
    DocumentInvoiceWorkflowDeleteDialogComponent,
  ],
  entryComponents: [DocumentInvoiceWorkflowDeleteDialogComponent],
})
export class DocumentInvoiceWorkflowModule {}
