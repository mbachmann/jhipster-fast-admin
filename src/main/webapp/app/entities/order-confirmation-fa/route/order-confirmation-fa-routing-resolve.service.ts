import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrderConfirmationFa, OrderConfirmationFa } from '../order-confirmation-fa.model';
import { OrderConfirmationFaService } from '../service/order-confirmation-fa.service';

@Injectable({ providedIn: 'root' })
export class OrderConfirmationFaRoutingResolveService implements Resolve<IOrderConfirmationFa> {
  constructor(protected service: OrderConfirmationFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrderConfirmationFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orderConfirmation: HttpResponse<OrderConfirmationFa>) => {
          if (orderConfirmation.body) {
            return of(orderConfirmation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrderConfirmationFa());
  }
}
