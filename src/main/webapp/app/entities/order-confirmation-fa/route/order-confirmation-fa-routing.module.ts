import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrderConfirmationFaComponent } from '../list/order-confirmation-fa.component';
import { OrderConfirmationFaDetailComponent } from '../detail/order-confirmation-fa-detail.component';
import { OrderConfirmationFaUpdateComponent } from '../update/order-confirmation-fa-update.component';
import { OrderConfirmationFaRoutingResolveService } from './order-confirmation-fa-routing-resolve.service';

const orderConfirmationRoute: Routes = [
  {
    path: '',
    component: OrderConfirmationFaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrderConfirmationFaDetailComponent,
    resolve: {
      orderConfirmation: OrderConfirmationFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrderConfirmationFaUpdateComponent,
    resolve: {
      orderConfirmation: OrderConfirmationFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrderConfirmationFaUpdateComponent,
    resolve: {
      orderConfirmation: OrderConfirmationFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orderConfirmationRoute)],
  exports: [RouterModule],
})
export class OrderConfirmationFaRoutingModule {}
