import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResourcePermissionComponent } from '../list/resource-permission.component';
import { ResourcePermissionDetailComponent } from '../detail/resource-permission-detail.component';
import { ResourcePermissionUpdateComponent } from '../update/resource-permission-update.component';
import { ResourcePermissionRoutingResolveService } from './resource-permission-routing-resolve.service';

const resourcePermissionRoute: Routes = [
  {
    path: '',
    component: ResourcePermissionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResourcePermissionDetailComponent,
    resolve: {
      resourcePermission: ResourcePermissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResourcePermissionUpdateComponent,
    resolve: {
      resourcePermission: ResourcePermissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResourcePermissionUpdateComponent,
    resolve: {
      resourcePermission: ResourcePermissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(resourcePermissionRoute)],
  exports: [RouterModule],
})
export class ResourcePermissionRoutingModule {}
