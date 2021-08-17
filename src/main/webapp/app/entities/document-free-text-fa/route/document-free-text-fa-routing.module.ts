import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentFreeTextFaComponent } from '../list/document-free-text-fa.component';
import { DocumentFreeTextFaDetailComponent } from '../detail/document-free-text-fa-detail.component';
import { DocumentFreeTextFaUpdateComponent } from '../update/document-free-text-fa-update.component';
import { DocumentFreeTextFaRoutingResolveService } from './document-free-text-fa-routing-resolve.service';

const documentFreeTextRoute: Routes = [
  {
    path: '',
    component: DocumentFreeTextFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentFreeTextFaDetailComponent,
    resolve: {
      documentFreeText: DocumentFreeTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentFreeTextFaUpdateComponent,
    resolve: {
      documentFreeText: DocumentFreeTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentFreeTextFaUpdateComponent,
    resolve: {
      documentFreeText: DocumentFreeTextFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentFreeTextRoute)],
  exports: [RouterModule],
})
export class DocumentFreeTextFaRoutingModule {}
