import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExchangeRateFaComponent } from '../list/exchange-rate-fa.component';
import { ExchangeRateFaDetailComponent } from '../detail/exchange-rate-fa-detail.component';
import { ExchangeRateFaUpdateComponent } from '../update/exchange-rate-fa-update.component';
import { ExchangeRateFaRoutingResolveService } from './exchange-rate-fa-routing-resolve.service';

const exchangeRateRoute: Routes = [
  {
    path: '',
    component: ExchangeRateFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExchangeRateFaDetailComponent,
    resolve: {
      exchangeRate: ExchangeRateFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExchangeRateFaUpdateComponent,
    resolve: {
      exchangeRate: ExchangeRateFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExchangeRateFaUpdateComponent,
    resolve: {
      exchangeRate: ExchangeRateFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(exchangeRateRoute)],
  exports: [RouterModule],
})
export class ExchangeRateFaRoutingModule {}
