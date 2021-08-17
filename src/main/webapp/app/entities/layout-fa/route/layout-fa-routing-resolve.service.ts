import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILayoutFa, LayoutFa } from '../layout-fa.model';
import { LayoutFaService } from '../service/layout-fa.service';

@Injectable({ providedIn: 'root' })
export class LayoutFaRoutingResolveService implements Resolve<ILayoutFa> {
  constructor(protected service: LayoutFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILayoutFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((layout: HttpResponse<LayoutFa>) => {
          if (layout.body) {
            return of(layout.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LayoutFa());
  }
}
