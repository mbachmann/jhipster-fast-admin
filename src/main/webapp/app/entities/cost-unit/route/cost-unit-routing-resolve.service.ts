import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICostUnit, CostUnit } from '../cost-unit.model';
import { CostUnitService } from '../service/cost-unit.service';

@Injectable({ providedIn: 'root' })
export class CostUnitRoutingResolveService implements Resolve<ICostUnit> {
  constructor(protected service: CostUnitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICostUnit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((costUnit: HttpResponse<CostUnit>) => {
          if (costUnit.body) {
            return of(costUnit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CostUnit());
  }
}
