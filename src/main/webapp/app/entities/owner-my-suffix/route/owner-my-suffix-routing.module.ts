import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OwnerMySuffixComponent } from '../list/owner-my-suffix.component';
import { OwnerMySuffixDetailComponent } from '../detail/owner-my-suffix-detail.component';
import { OwnerMySuffixUpdateComponent } from '../update/owner-my-suffix-update.component';
import { OwnerMySuffixRoutingResolveService } from './owner-my-suffix-routing-resolve.service';

const ownerRoute: Routes = [
  {
    path: '',
    component: OwnerMySuffixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OwnerMySuffixDetailComponent,
    resolve: {
      owner: OwnerMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OwnerMySuffixUpdateComponent,
    resolve: {
      owner: OwnerMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OwnerMySuffixUpdateComponent,
    resolve: {
      owner: OwnerMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ownerRoute)],
  exports: [RouterModule],
})
export class OwnerMySuffixRoutingModule {}
