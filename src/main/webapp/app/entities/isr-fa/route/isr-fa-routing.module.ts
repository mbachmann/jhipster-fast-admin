import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IsrFaComponent } from '../list/isr-fa.component';
import { IsrFaDetailComponent } from '../detail/isr-fa-detail.component';
import { IsrFaUpdateComponent } from '../update/isr-fa-update.component';
import { IsrFaRoutingResolveService } from './isr-fa-routing-resolve.service';

const isrRoute: Routes = [
  {
    path: '',
    component: IsrFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IsrFaDetailComponent,
    resolve: {
      isr: IsrFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IsrFaUpdateComponent,
    resolve: {
      isr: IsrFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IsrFaUpdateComponent,
    resolve: {
      isr: IsrFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(isrRoute)],
  exports: [RouterModule],
})
export class IsrFaRoutingModule {}
