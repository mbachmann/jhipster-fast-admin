import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IsrComponent } from '../list/isr.component';
import { IsrDetailComponent } from '../detail/isr-detail.component';
import { IsrUpdateComponent } from '../update/isr-update.component';
import { IsrRoutingResolveService } from './isr-routing-resolve.service';

const isrRoute: Routes = [
  {
    path: '',
    component: IsrComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IsrDetailComponent,
    resolve: {
      isr: IsrRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IsrUpdateComponent,
    resolve: {
      isr: IsrRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IsrUpdateComponent,
    resolve: {
      isr: IsrRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(isrRoute)],
  exports: [RouterModule],
})
export class IsrRoutingModule {}
