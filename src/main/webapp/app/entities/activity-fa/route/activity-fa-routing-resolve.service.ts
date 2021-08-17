import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IActivityFa, ActivityFa } from '../activity-fa.model';
import { ActivityFaService } from '../service/activity-fa.service';

@Injectable({ providedIn: 'root' })
export class ActivityFaRoutingResolveService implements Resolve<IActivityFa> {
  constructor(protected service: ActivityFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IActivityFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((activity: HttpResponse<ActivityFa>) => {
          if (activity.body) {
            return of(activity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ActivityFa());
  }
}
