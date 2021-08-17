import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentInvoiceWorkflowComponent } from '../list/document-invoice-workflow.component';
import { DocumentInvoiceWorkflowDetailComponent } from '../detail/document-invoice-workflow-detail.component';
import { DocumentInvoiceWorkflowUpdateComponent } from '../update/document-invoice-workflow-update.component';
import { DocumentInvoiceWorkflowRoutingResolveService } from './document-invoice-workflow-routing-resolve.service';

const documentInvoiceWorkflowRoute: Routes = [
  {
    path: '',
    component: DocumentInvoiceWorkflowComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentInvoiceWorkflowDetailComponent,
    resolve: {
      documentInvoiceWorkflow: DocumentInvoiceWorkflowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentInvoiceWorkflowUpdateComponent,
    resolve: {
      documentInvoiceWorkflow: DocumentInvoiceWorkflowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentInvoiceWorkflowUpdateComponent,
    resolve: {
      documentInvoiceWorkflow: DocumentInvoiceWorkflowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentInvoiceWorkflowRoute)],
  exports: [RouterModule],
})
export class DocumentInvoiceWorkflowRoutingModule {}
