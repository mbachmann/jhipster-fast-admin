import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOfferFa, OfferFa } from '../offer-fa.model';
import { OfferFaService } from '../service/offer-fa.service';

@Injectable({ providedIn: 'root' })
export class OfferFaRoutingResolveService implements Resolve<IOfferFa> {
  constructor(protected service: OfferFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOfferFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((offer: HttpResponse<OfferFa>) => {
          if (offer.body) {
            return of(offer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OfferFa());
  }
}
