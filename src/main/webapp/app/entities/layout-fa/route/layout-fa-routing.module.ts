import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LayoutFaComponent } from '../list/layout-fa.component';
import { LayoutFaDetailComponent } from '../detail/layout-fa-detail.component';
import { LayoutFaUpdateComponent } from '../update/layout-fa-update.component';
import { LayoutFaRoutingResolveService } from './layout-fa-routing-resolve.service';

const layoutRoute: Routes = [
  {
    path: '',
    component: LayoutFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LayoutFaDetailComponent,
    resolve: {
      layout: LayoutFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LayoutFaUpdateComponent,
    resolve: {
      layout: LayoutFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LayoutFaUpdateComponent,
    resolve: {
      layout: LayoutFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(layoutRoute)],
  exports: [RouterModule],
})
export class LayoutFaRoutingModule {}
