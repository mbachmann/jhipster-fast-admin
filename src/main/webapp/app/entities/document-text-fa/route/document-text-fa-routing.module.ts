import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentTextFaComponent } from '../list/document-text-fa.component';
import { DocumentTextFaDetailComponent } from '../detail/document-text-fa-detail.component';
import { DocumentTextFaUpdateComponent } from '../update/document-text-fa-update.component';
import { DocumentTextFaRoutingResolveService } from './document-text-fa-routing-resolve.service';

const documentTextRoute: Routes = [
  {
    path: '',
    component: DocumentTextFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentTextFaDetailComponent,
    resolve: {
      documentText: DocumentTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentTextFaUpdateComponent,
    resolve: {
      documentText: DocumentTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentTextFaUpdateComponent,
    resolve: {
      documentText: DocumentTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentTextRoute)],
  exports: [RouterModule],
})
export class DocumentTextFaRoutingModule {}
