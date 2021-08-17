import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvoiceFa, InvoiceFa } from '../invoice-fa.model';
import { InvoiceFaService } from '../service/invoice-fa.service';

@Injectable({ providedIn: 'root' })
export class InvoiceFaRoutingResolveService implements Resolve<IInvoiceFa> {
  constructor(protected service: InvoiceFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvoiceFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((invoice: HttpResponse<InvoiceFa>) => {
          if (invoice.body) {
            return of(invoice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InvoiceFa());
  }
}
