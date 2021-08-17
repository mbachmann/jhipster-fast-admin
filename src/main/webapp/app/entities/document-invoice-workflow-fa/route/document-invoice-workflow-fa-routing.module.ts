import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentInvoiceWorkflowFaComponent } from '../list/document-invoice-workflow-fa.component';
import { DocumentInvoiceWorkflowFaDetailComponent } from '../detail/document-invoice-workflow-fa-detail.component';
import { DocumentInvoiceWorkflowFaUpdateComponent } from '../update/document-invoice-workflow-fa-update.component';
import { DocumentInvoiceWorkflowFaRoutingResolveService } from './document-invoice-workflow-fa-routing-resolve.service';

const documentInvoiceWorkflowRoute: Routes = [
  {
    path: '',
    component: DocumentInvoiceWorkflowFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentInvoiceWorkflowFaDetailComponent,
    resolve: {
      documentInvoiceWorkflow: DocumentInvoiceWorkflowFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentInvoiceWorkflowFaUpdateComponent,
    resolve: {
      documentInvoiceWorkflow: DocumentInvoiceWorkflowFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentInvoiceWorkflowFaUpdateComponent,
    resolve: {
      documentInvoiceWorkflow: DocumentInvoiceWorkflowFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentInvoiceWorkflowRoute)],
  exports: [RouterModule],
})
export class DocumentInvoiceWorkflowFaRoutingModule {}
