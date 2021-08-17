import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExchangeRateFa, ExchangeRateFa } from '../exchange-rate-fa.model';
import { ExchangeRateFaService } from '../service/exchange-rate-fa.service';

@Injectable({ providedIn: 'root' })
export class ExchangeRateFaRoutingResolveService implements Resolve<IExchangeRateFa> {
  constructor(protected service: ExchangeRateFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExchangeRateFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((exchangeRate: HttpResponse<ExchangeRateFa>) => {
          if (exchangeRate.body) {
            return of(exchangeRate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExchangeRateFa());
  }
}
