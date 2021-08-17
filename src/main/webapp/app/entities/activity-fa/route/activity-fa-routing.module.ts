import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ActivityFaComponent } from '../list/activity-fa.component';
import { ActivityFaDetailComponent } from '../detail/activity-fa-detail.component';
import { ActivityFaUpdateComponent } from '../update/activity-fa-update.component';
import { ActivityFaRoutingResolveService } from './activity-fa-routing-resolve.service';

const activityRoute: Routes = [
  {
    path: '',
    component: ActivityFaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActivityFaDetailComponent,
    resolve: {
      activity: ActivityFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ActivityFaUpdateComponent,
    resolve: {
      activity: ActivityFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ActivityFaUpdateComponent,
    resolve: {
      activity: ActivityFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(activityRoute)],
  exports: [RouterModule],
})
export class ActivityFaRoutingModule {}
