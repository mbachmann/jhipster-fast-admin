import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrderConfirmationComponent } from '../list/order-confirmation.component';
import { OrderConfirmationDetailComponent } from '../detail/order-confirmation-detail.component';
import { OrderConfirmationUpdateComponent } from '../update/order-confirmation-update.component';
import { OrderConfirmationRoutingResolveService } from './order-confirmation-routing-resolve.service';

const orderConfirmationRoute: Routes = [
  {
    path: '',
    component: OrderConfirmationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrderConfirmationDetailComponent,
    resolve: {
      orderConfirmation: OrderConfirmationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrderConfirmationUpdateComponent,
    resolve: {
      orderConfirmation: OrderConfirmationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrderConfirmationUpdateComponent,
    resolve: {
      orderConfirmation: OrderConfirmationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orderConfirmationRoute)],
  exports: [RouterModule],
})
export class OrderConfirmationRoutingModule {}
