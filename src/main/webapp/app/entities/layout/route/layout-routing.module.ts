import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LayoutComponent } from '../list/layout.component';
import { LayoutDetailComponent } from '../detail/layout-detail.component';
import { LayoutUpdateComponent } from '../update/layout-update.component';
import { LayoutRoutingResolveService } from './layout-routing-resolve.service';

const layoutRoute: Routes = [
  {
    path: '',
    component: LayoutComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LayoutDetailComponent,
    resolve: {
      layout: LayoutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LayoutUpdateComponent,
    resolve: {
      layout: LayoutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LayoutUpdateComponent,
    resolve: {
      layout: LayoutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(layoutRoute)],
  exports: [RouterModule],
})
export class LayoutRoutingModule {}
