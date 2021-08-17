import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFreeTextFa, FreeTextFa } from '../free-text-fa.model';
import { FreeTextFaService } from '../service/free-text-fa.service';

@Injectable({ providedIn: 'root' })
export class FreeTextFaRoutingResolveService implements Resolve<IFreeTextFa> {
  constructor(protected service: FreeTextFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFreeTextFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((freeText: HttpResponse<FreeTextFa>) => {
          if (freeText.body) {
            return of(freeText.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FreeTextFa());
  }
}
