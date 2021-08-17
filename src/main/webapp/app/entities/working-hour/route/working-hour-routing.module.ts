import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkingHourComponent } from '../list/working-hour.component';
import { WorkingHourDetailComponent } from '../detail/working-hour-detail.component';
import { WorkingHourUpdateComponent } from '../update/working-hour-update.component';
import { WorkingHourRoutingResolveService } from './working-hour-routing-resolve.service';

const workingHourRoute: Routes = [
  {
    path: '',
    component: WorkingHourComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkingHourDetailComponent,
    resolve: {
      workingHour: WorkingHourRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkingHourUpdateComponent,
    resolve: {
      workingHour: WorkingHourRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkingHourUpdateComponent,
    resolve: {
      workingHour: WorkingHourRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workingHourRoute)],
  exports: [RouterModule],
})
export class WorkingHourRoutingModule {}
