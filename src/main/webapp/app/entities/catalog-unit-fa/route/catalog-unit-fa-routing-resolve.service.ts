import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogUnitFa, CatalogUnitFa } from '../catalog-unit-fa.model';
import { CatalogUnitFaService } from '../service/catalog-unit-fa.service';

@Injectable({ providedIn: 'root' })
export class CatalogUnitFaRoutingResolveService implements Resolve<ICatalogUnitFa> {
  constructor(protected service: CatalogUnitFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogUnitFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogUnit: HttpResponse<CatalogUnitFa>) => {
          if (catalogUnit.body) {
            return of(catalogUnit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CatalogUnitFa());
  }
}
