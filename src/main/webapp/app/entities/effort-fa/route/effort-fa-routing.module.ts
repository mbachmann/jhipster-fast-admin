import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EffortFaComponent } from '../list/effort-fa.component';
import { EffortFaDetailComponent } from '../detail/effort-fa-detail.component';
import { EffortFaUpdateComponent } from '../update/effort-fa-update.component';
import { EffortFaRoutingResolveService } from './effort-fa-routing-resolve.service';

const effortRoute: Routes = [
  {
    path: '',
    component: EffortFaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EffortFaDetailComponent,
    resolve: {
      effort: EffortFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EffortFaUpdateComponent,
    resolve: {
      effort: EffortFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EffortFaUpdateComponent,
    resolve: {
      effort: EffortFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(effortRoute)],
  exports: [RouterModule],
})
export class EffortFaRoutingModule {}
