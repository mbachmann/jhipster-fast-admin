import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApplicationRoleComponent } from '../list/application-role.component';
import { ApplicationRoleDetailComponent } from '../detail/application-role-detail.component';
import { ApplicationRoleUpdateComponent } from '../update/application-role-update.component';
import { ApplicationRoleRoutingResolveService } from './application-role-routing-resolve.service';

const applicationRoleRoute: Routes = [
  {
    path: '',
    component: ApplicationRoleComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApplicationRoleDetailComponent,
    resolve: {
      applicationRole: ApplicationRoleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApplicationRoleUpdateComponent,
    resolve: {
      applicationRole: ApplicationRoleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApplicationRoleUpdateComponent,
    resolve: {
      applicationRole: ApplicationRoleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(applicationRoleRoute)],
  exports: [RouterModule],
})
export class ApplicationRoleRoutingModule {}
