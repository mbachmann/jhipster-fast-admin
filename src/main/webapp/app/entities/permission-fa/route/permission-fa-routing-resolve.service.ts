import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPermissionFa, PermissionFa } from '../permission-fa.model';
import { PermissionFaService } from '../service/permission-fa.service';

@Injectable({ providedIn: 'root' })
export class PermissionFaRoutingResolveService implements Resolve<IPermissionFa> {
  constructor(protected service: PermissionFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPermissionFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((permission: HttpResponse<PermissionFa>) => {
          if (permission.body) {
            return of(permission.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PermissionFa());
  }
}
