import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ValueAddedTaxComponent } from '../list/value-added-tax.component';
import { ValueAddedTaxDetailComponent } from '../detail/value-added-tax-detail.component';
import { ValueAddedTaxUpdateComponent } from '../update/value-added-tax-update.component';
import { ValueAddedTaxRoutingResolveService } from './value-added-tax-routing-resolve.service';

const valueAddedTaxRoute: Routes = [
  {
    path: '',
    component: ValueAddedTaxComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ValueAddedTaxDetailComponent,
    resolve: {
      valueAddedTax: ValueAddedTaxRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ValueAddedTaxUpdateComponent,
    resolve: {
      valueAddedTax: ValueAddedTaxRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ValueAddedTaxUpdateComponent,
    resolve: {
      valueAddedTax: ValueAddedTaxRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(valueAddedTaxRoute)],
  exports: [RouterModule],
})
export class ValueAddedTaxRoutingModule {}
