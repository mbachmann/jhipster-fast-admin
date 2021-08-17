import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentPositionFaComponent } from '../list/document-position-fa.component';
import { DocumentPositionFaDetailComponent } from '../detail/document-position-fa-detail.component';
import { DocumentPositionFaUpdateComponent } from '../update/document-position-fa-update.component';
import { DocumentPositionFaRoutingResolveService } from './document-position-fa-routing-resolve.service';

const documentPositionRoute: Routes = [
  {
    path: '',
    component: DocumentPositionFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentPositionFaDetailComponent,
    resolve: {
      documentPosition: DocumentPositionFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentPositionFaUpdateComponent,
    resolve: {
      documentPosition: DocumentPositionFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentPositionFaUpdateComponent,
    resolve: {
      documentPosition: DocumentPositionFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentPositionRoute)],
  exports: [RouterModule],
})
export class DocumentPositionFaRoutingModule {}
