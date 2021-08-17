import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogUnit, CatalogUnit } from '../catalog-unit.model';
import { CatalogUnitService } from '../service/catalog-unit.service';

@Injectable({ providedIn: 'root' })
export class CatalogUnitRoutingResolveService implements Resolve<ICatalogUnit> {
  constructor(protected service: CatalogUnitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogUnit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogUnit: HttpResponse<CatalogUnit>) => {
          if (catalogUnit.body) {
            return of(catalogUnit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CatalogUnit());
  }
}
