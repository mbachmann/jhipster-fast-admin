import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PermissionMySuffixComponent } from '../list/permission-my-suffix.component';
import { PermissionMySuffixDetailComponent } from '../detail/permission-my-suffix-detail.component';
import { PermissionMySuffixUpdateComponent } from '../update/permission-my-suffix-update.component';
import { PermissionMySuffixRoutingResolveService } from './permission-my-suffix-routing-resolve.service';

const permissionRoute: Routes = [
  {
    path: '',
    component: PermissionMySuffixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PermissionMySuffixDetailComponent,
    resolve: {
      permission: PermissionMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PermissionMySuffixUpdateComponent,
    resolve: {
      permission: PermissionMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PermissionMySuffixUpdateComponent,
    resolve: {
      permission: PermissionMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(permissionRoute)],
  exports: [RouterModule],
})
export class PermissionMySuffixRoutingModule {}
