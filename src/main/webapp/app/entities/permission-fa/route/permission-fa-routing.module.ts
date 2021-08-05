import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PermissionFaComponent } from '../list/permission-fa.component';
import { PermissionFaDetailComponent } from '../detail/permission-fa-detail.component';
import { PermissionFaUpdateComponent } from '../update/permission-fa-update.component';
import { PermissionFaRoutingResolveService } from './permission-fa-routing-resolve.service';

const permissionRoute: Routes = [
  {
    path: '',
    component: PermissionFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PermissionFaDetailComponent,
    resolve: {
      permission: PermissionFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PermissionFaUpdateComponent,
    resolve: {
      permission: PermissionFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PermissionFaUpdateComponent,
    resolve: {
      permission: PermissionFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(permissionRoute)],
  exports: [RouterModule],
})
export class PermissionFaRoutingModule {}
