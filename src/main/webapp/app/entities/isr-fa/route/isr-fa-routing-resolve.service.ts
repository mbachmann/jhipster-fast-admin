import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIsrFa, IsrFa } from '../isr-fa.model';
import { IsrFaService } from '../service/isr-fa.service';

@Injectable({ providedIn: 'root' })
export class IsrFaRoutingResolveService implements Resolve<IIsrFa> {
  constructor(protected service: IsrFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIsrFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((isr: HttpResponse<IsrFa>) => {
          if (isr.body) {
            return of(isr.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IsrFa());
  }
}
