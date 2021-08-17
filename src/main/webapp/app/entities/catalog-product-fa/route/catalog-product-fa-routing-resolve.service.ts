import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogProductFa, CatalogProductFa } from '../catalog-product-fa.model';
import { CatalogProductFaService } from '../service/catalog-product-fa.service';

@Injectable({ providedIn: 'root' })
export class CatalogProductFaRoutingResolveService implements Resolve<ICatalogProductFa> {
  constructor(protected service: CatalogProductFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogProductFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogProduct: HttpResponse<CatalogProductFa>) => {
          if (catalogProduct.body) {
            return of(catalogProduct.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CatalogProductFa());
  }
}
