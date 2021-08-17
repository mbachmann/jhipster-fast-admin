import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEffortFa, EffortFa } from '../effort-fa.model';
import { EffortFaService } from '../service/effort-fa.service';

@Injectable({ providedIn: 'root' })
export class EffortFaRoutingResolveService implements Resolve<IEffortFa> {
  constructor(protected service: EffortFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEffortFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((effort: HttpResponse<EffortFa>) => {
          if (effort.body) {
            return of(effort.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EffortFa());
  }
}
