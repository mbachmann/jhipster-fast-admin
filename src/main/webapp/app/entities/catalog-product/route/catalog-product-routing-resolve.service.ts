import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogProduct, CatalogProduct } from '../catalog-product.model';
import { CatalogProductService } from '../service/catalog-product.service';

@Injectable({ providedIn: 'root' })
export class CatalogProductRoutingResolveService implements Resolve<ICatalogProduct> {
  constructor(protected service: CatalogProductService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogProduct> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogProduct: HttpResponse<CatalogProduct>) => {
          if (catalogProduct.body) {
            return of(catalogProduct.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CatalogProduct());
  }
}
