import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CostUnitComponent } from '../list/cost-unit.component';
import { CostUnitDetailComponent } from '../detail/cost-unit-detail.component';
import { CostUnitUpdateComponent } from '../update/cost-unit-update.component';
import { CostUnitRoutingResolveService } from './cost-unit-routing-resolve.service';

const costUnitRoute: Routes = [
  {
    path: '',
    component: CostUnitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CostUnitDetailComponent,
    resolve: {
      costUnit: CostUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CostUnitUpdateComponent,
    resolve: {
      costUnit: CostUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CostUnitUpdateComponent,
    resolve: {
      costUnit: CostUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(costUnitRoute)],
  exports: [RouterModule],
})
export class CostUnitRoutingModule {}
