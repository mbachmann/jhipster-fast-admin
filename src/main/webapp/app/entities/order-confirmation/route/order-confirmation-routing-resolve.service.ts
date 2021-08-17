import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrderConfirmation, OrderConfirmation } from '../order-confirmation.model';
import { OrderConfirmationService } from '../service/order-confirmation.service';

@Injectable({ providedIn: 'root' })
export class OrderConfirmationRoutingResolveService implements Resolve<IOrderConfirmation> {
  constructor(protected service: OrderConfirmationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrderConfirmation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orderConfirmation: HttpResponse<OrderConfirmation>) => {
          if (orderConfirmation.body) {
            return of(orderConfirmation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrderConfirmation());
  }
}
