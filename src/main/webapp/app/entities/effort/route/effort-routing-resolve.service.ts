import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEffort, Effort } from '../effort.model';
import { EffortService } from '../service/effort.service';

@Injectable({ providedIn: 'root' })
export class EffortRoutingResolveService implements Resolve<IEffort> {
  constructor(protected service: EffortService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEffort> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((effort: HttpResponse<Effort>) => {
          if (effort.body) {
            return of(effort.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Effort());
  }
}
