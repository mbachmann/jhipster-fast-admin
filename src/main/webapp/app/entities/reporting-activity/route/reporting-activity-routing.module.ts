import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportingActivityComponent } from '../list/reporting-activity.component';
import { ReportingActivityDetailComponent } from '../detail/reporting-activity-detail.component';
import { ReportingActivityUpdateComponent } from '../update/reporting-activity-update.component';
import { ReportingActivityRoutingResolveService } from './reporting-activity-routing-resolve.service';

const reportingActivityRoute: Routes = [
  {
    path: '',
    component: ReportingActivityComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportingActivityDetailComponent,
    resolve: {
      reportingActivity: ReportingActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportingActivityUpdateComponent,
    resolve: {
      reportingActivity: ReportingActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportingActivityUpdateComponent,
    resolve: {
      reportingActivity: ReportingActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reportingActivityRoute)],
  exports: [RouterModule],
})
export class ReportingActivityRoutingModule {}
