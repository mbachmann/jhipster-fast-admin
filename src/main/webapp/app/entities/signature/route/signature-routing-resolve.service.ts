import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISignature, Signature } from '../signature.model';
import { SignatureService } from '../service/signature.service';

@Injectable({ providedIn: 'root' })
export class SignatureRoutingResolveService implements Resolve<ISignature> {
  constructor(protected service: SignatureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISignature> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((signature: HttpResponse<Signature>) => {
          if (signature.body) {
            return of(signature.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Signature());
  }
}
