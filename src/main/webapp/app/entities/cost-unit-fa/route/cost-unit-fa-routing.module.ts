import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CostUnitFaComponent } from '../list/cost-unit-fa.component';
import { CostUnitFaDetailComponent } from '../detail/cost-unit-fa-detail.component';
import { CostUnitFaUpdateComponent } from '../update/cost-unit-fa-update.component';
import { CostUnitFaRoutingResolveService } from './cost-unit-fa-routing-resolve.service';

const costUnitRoute: Routes = [
  {
    path: '',
    component: CostUnitFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CostUnitFaDetailComponent,
    resolve: {
      costUnit: CostUnitFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CostUnitFaUpdateComponent,
    resolve: {
      costUnit: CostUnitFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CostUnitFaUpdateComponent,
    resolve: {
      costUnit: CostUnitFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(costUnitRoute)],
  exports: [RouterModule],
})
export class CostUnitFaRoutingModule {}
