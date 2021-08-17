import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EffortComponent } from '../list/effort.component';
import { EffortDetailComponent } from '../detail/effort-detail.component';
import { EffortUpdateComponent } from '../update/effort-update.component';
import { EffortRoutingResolveService } from './effort-routing-resolve.service';

const effortRoute: Routes = [
  {
    path: '',
    component: EffortComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EffortDetailComponent,
    resolve: {
      effort: EffortRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EffortUpdateComponent,
    resolve: {
      effort: EffortRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EffortUpdateComponent,
    resolve: {
      effort: EffortRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(effortRoute)],
  exports: [RouterModule],
})
export class EffortRoutingModule {}
