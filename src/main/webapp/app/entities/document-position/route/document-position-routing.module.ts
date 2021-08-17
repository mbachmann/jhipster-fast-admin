import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentPositionComponent } from '../list/document-position.component';
import { DocumentPositionDetailComponent } from '../detail/document-position-detail.component';
import { DocumentPositionUpdateComponent } from '../update/document-position-update.component';
import { DocumentPositionRoutingResolveService } from './document-position-routing-resolve.service';

const documentPositionRoute: Routes = [
  {
    path: '',
    component: DocumentPositionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentPositionDetailComponent,
    resolve: {
      documentPosition: DocumentPositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentPositionUpdateComponent,
    resolve: {
      documentPosition: DocumentPositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentPositionUpdateComponent,
    resolve: {
      documentPosition: DocumentPositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentPositionRoute)],
  exports: [RouterModule],
})
export class DocumentPositionRoutingModule {}
