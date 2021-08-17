import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISignatureFa, SignatureFa } from '../signature-fa.model';
import { SignatureFaService } from '../service/signature-fa.service';

@Injectable({ providedIn: 'root' })
export class SignatureFaRoutingResolveService implements Resolve<ISignatureFa> {
  constructor(protected service: SignatureFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISignatureFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((signature: HttpResponse<SignatureFa>) => {
          if (signature.body) {
            return of(signature.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SignatureFa());
  }
}
