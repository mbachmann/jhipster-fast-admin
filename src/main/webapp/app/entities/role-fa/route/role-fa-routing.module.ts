import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoleFaComponent } from '../list/role-fa.component';
import { RoleFaDetailComponent } from '../detail/role-fa-detail.component';
import { RoleFaUpdateComponent } from '../update/role-fa-update.component';
import { RoleFaRoutingResolveService } from './role-fa-routing-resolve.service';

const roleRoute: Routes = [
  {
    path: '',
    component: RoleFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoleFaDetailComponent,
    resolve: {
      role: RoleFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoleFaUpdateComponent,
    resolve: {
      role: RoleFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoleFaUpdateComponent,
    resolve: {
      role: RoleFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roleRoute)],
  exports: [RouterModule],
})
export class RoleFaRoutingModule {}
