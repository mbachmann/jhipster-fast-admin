import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OfferFaComponent } from '../list/offer-fa.component';
import { OfferFaDetailComponent } from '../detail/offer-fa-detail.component';
import { OfferFaUpdateComponent } from '../update/offer-fa-update.component';
import { OfferFaRoutingResolveService } from './offer-fa-routing-resolve.service';

const offerRoute: Routes = [
  {
    path: '',
    component: OfferFaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OfferFaDetailComponent,
    resolve: {
      offer: OfferFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OfferFaUpdateComponent,
    resolve: {
      offer: OfferFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OfferFaUpdateComponent,
    resolve: {
      offer: OfferFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(offerRoute)],
  exports: [RouterModule],
})
export class OfferFaRoutingModule {}
