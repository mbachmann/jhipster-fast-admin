import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentTextComponent } from '../list/document-text.component';
import { DocumentTextDetailComponent } from '../detail/document-text-detail.component';
import { DocumentTextUpdateComponent } from '../update/document-text-update.component';
import { DocumentTextRoutingResolveService } from './document-text-routing-resolve.service';

const documentTextRoute: Routes = [
  {
    path: '',
    component: DocumentTextComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentTextDetailComponent,
    resolve: {
      documentText: DocumentTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentTextUpdateComponent,
    resolve: {
      documentText: DocumentTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentTextUpdateComponent,
    resolve: {
      documentText: DocumentTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentTextRoute)],
  exports: [RouterModule],
})
export class DocumentTextRoutingModule {}
