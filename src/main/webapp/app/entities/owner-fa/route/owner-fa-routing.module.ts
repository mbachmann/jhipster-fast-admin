import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OwnerFaComponent } from '../list/owner-fa.component';
import { OwnerFaDetailComponent } from '../detail/owner-fa-detail.component';
import { OwnerFaUpdateComponent } from '../update/owner-fa-update.component';
import { OwnerFaRoutingResolveService } from './owner-fa-routing-resolve.service';

const ownerRoute: Routes = [
  {
    path: '',
    component: OwnerFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OwnerFaDetailComponent,
    resolve: {
      owner: OwnerFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OwnerFaUpdateComponent,
    resolve: {
      owner: OwnerFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OwnerFaUpdateComponent,
    resolve: {
      owner: OwnerFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ownerRoute)],
  exports: [RouterModule],
})
export class OwnerFaRoutingModule {}
