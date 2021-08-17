import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkingHour, WorkingHour } from '../working-hour.model';
import { WorkingHourService } from '../service/working-hour.service';

@Injectable({ providedIn: 'root' })
export class WorkingHourRoutingResolveService implements Resolve<IWorkingHour> {
  constructor(protected service: WorkingHourService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkingHour> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workingHour: HttpResponse<WorkingHour>) => {
          if (workingHour.body) {
            return of(workingHour.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WorkingHour());
  }
}
