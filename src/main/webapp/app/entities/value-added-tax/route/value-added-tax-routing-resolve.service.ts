import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IValueAddedTax, ValueAddedTax } from '../value-added-tax.model';
import { ValueAddedTaxService } from '../service/value-added-tax.service';

@Injectable({ providedIn: 'root' })
export class ValueAddedTaxRoutingResolveService implements Resolve<IValueAddedTax> {
  constructor(protected service: ValueAddedTaxService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IValueAddedTax> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((valueAddedTax: HttpResponse<ValueAddedTax>) => {
          if (valueAddedTax.body) {
            return of(valueAddedTax.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ValueAddedTax());
  }
}
