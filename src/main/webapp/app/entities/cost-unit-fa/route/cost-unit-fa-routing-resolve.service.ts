import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICostUnitFa, CostUnitFa } from '../cost-unit-fa.model';
import { CostUnitFaService } from '../service/cost-unit-fa.service';

@Injectable({ providedIn: 'root' })
export class CostUnitFaRoutingResolveService implements Resolve<ICostUnitFa> {
  constructor(protected service: CostUnitFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICostUnitFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((costUnit: HttpResponse<CostUnitFa>) => {
          if (costUnit.body) {
            return of(costUnit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CostUnitFa());
  }
}
