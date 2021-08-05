import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoleMySuffixComponent } from '../list/role-my-suffix.component';
import { RoleMySuffixDetailComponent } from '../detail/role-my-suffix-detail.component';
import { RoleMySuffixUpdateComponent } from '../update/role-my-suffix-update.component';
import { RoleMySuffixRoutingResolveService } from './role-my-suffix-routing-resolve.service';

const roleRoute: Routes = [
  {
    path: '',
    component: RoleMySuffixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoleMySuffixDetailComponent,
    resolve: {
      role: RoleMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoleMySuffixUpdateComponent,
    resolve: {
      role: RoleMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoleMySuffixUpdateComponent,
    resolve: {
      role: RoleMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roleRoute)],
  exports: [RouterModule],
})
export class RoleMySuffixRoutingModule {}
