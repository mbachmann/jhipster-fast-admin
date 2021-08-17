import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeliveryNoteFaComponent } from '../list/delivery-note-fa.component';
import { DeliveryNoteFaDetailComponent } from '../detail/delivery-note-fa-detail.component';
import { DeliveryNoteFaUpdateComponent } from '../update/delivery-note-fa-update.component';
import { DeliveryNoteFaRoutingResolveService } from './delivery-note-fa-routing-resolve.service';

const deliveryNoteRoute: Routes = [
  {
    path: '',
    component: DeliveryNoteFaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeliveryNoteFaDetailComponent,
    resolve: {
      deliveryNote: DeliveryNoteFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeliveryNoteFaUpdateComponent,
    resolve: {
      deliveryNote: DeliveryNoteFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeliveryNoteFaUpdateComponent,
    resolve: {
      deliveryNote: DeliveryNoteFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deliveryNoteRoute)],
  exports: [RouterModule],
})
export class DeliveryNoteFaRoutingModule {}
