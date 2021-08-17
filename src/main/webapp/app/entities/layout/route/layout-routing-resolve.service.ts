import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILayout, Layout } from '../layout.model';
import { LayoutService } from '../service/layout.service';

@Injectable({ providedIn: 'root' })
export class LayoutRoutingResolveService implements Resolve<ILayout> {
  constructor(protected service: LayoutService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILayout> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((layout: HttpResponse<Layout>) => {
          if (layout.body) {
            return of(layout.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Layout());
  }
}
