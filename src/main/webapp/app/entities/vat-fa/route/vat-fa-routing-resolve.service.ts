import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVatFa, VatFa } from '../vat-fa.model';
import { VatFaService } from '../service/vat-fa.service';

@Injectable({ providedIn: 'root' })
export class VatFaRoutingResolveService implements Resolve<IVatFa> {
  constructor(protected service: VatFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVatFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vat: HttpResponse<VatFa>) => {
          if (vat.body) {
            return of(vat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new VatFa());
  }
}
