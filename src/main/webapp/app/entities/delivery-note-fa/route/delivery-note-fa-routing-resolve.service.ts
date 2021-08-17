import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeliveryNoteFa, DeliveryNoteFa } from '../delivery-note-fa.model';
import { DeliveryNoteFaService } from '../service/delivery-note-fa.service';

@Injectable({ providedIn: 'root' })
export class DeliveryNoteFaRoutingResolveService implements Resolve<IDeliveryNoteFa> {
  constructor(protected service: DeliveryNoteFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeliveryNoteFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deliveryNote: HttpResponse<DeliveryNoteFa>) => {
          if (deliveryNote.body) {
            return of(deliveryNote.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeliveryNoteFa());
  }
}
