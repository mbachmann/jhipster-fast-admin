import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProjectFaComponent } from '../list/project-fa.component';
import { ProjectFaDetailComponent } from '../detail/project-fa-detail.component';
import { ProjectFaUpdateComponent } from '../update/project-fa-update.component';
import { ProjectFaRoutingResolveService } from './project-fa-routing-resolve.service';

const projectRoute: Routes = [
  {
    path: '',
    component: ProjectFaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProjectFaDetailComponent,
    resolve: {
      project: ProjectFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProjectFaUpdateComponent,
    resolve: {
      project: ProjectFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProjectFaUpdateComponent,
    resolve: {
      project: ProjectFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(projectRoute)],
  exports: [RouterModule],
})
export class ProjectFaRoutingModule {}
