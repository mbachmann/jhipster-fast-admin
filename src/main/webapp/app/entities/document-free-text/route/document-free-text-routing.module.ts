import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentFreeTextComponent } from '../list/document-free-text.component';
import { DocumentFreeTextDetailComponent } from '../detail/document-free-text-detail.component';
import { DocumentFreeTextUpdateComponent } from '../update/document-free-text-update.component';
import { DocumentFreeTextRoutingResolveService } from './document-free-text-routing-resolve.service';

const documentFreeTextRoute: Routes = [
  {
    path: '',
    component: DocumentFreeTextComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentFreeTextDetailComponent,
    resolve: {
      documentFreeText: DocumentFreeTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentFreeTextUpdateComponent,
    resolve: {
      documentFreeText: DocumentFreeTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentFreeTextUpdateComponent,
    resolve: {
      documentFreeText: DocumentFreeTextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentFreeTextRoute)],
  exports: [RouterModule],
})
export class DocumentFreeTextRoutingModule {}
