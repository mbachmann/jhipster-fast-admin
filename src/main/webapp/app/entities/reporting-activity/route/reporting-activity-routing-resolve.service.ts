import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportingActivity, ReportingActivity } from '../reporting-activity.model';
import { ReportingActivityService } from '../service/reporting-activity.service';

@Injectable({ providedIn: 'root' })
export class ReportingActivityRoutingResolveService implements Resolve<IReportingActivity> {
  constructor(protected service: ReportingActivityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReportingActivity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reportingActivity: HttpResponse<ReportingActivity>) => {
          if (reportingActivity.body) {
            return of(reportingActivity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReportingActivity());
  }
}
