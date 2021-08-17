import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFreeText, FreeText } from '../free-text.model';
import { FreeTextService } from '../service/free-text.service';

@Injectable({ providedIn: 'root' })
export class FreeTextRoutingResolveService implements Resolve<IFreeText> {
  constructor(protected service: FreeTextService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFreeText> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((freeText: HttpResponse<FreeText>) => {
          if (freeText.body) {
            return of(freeText.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FreeText());
  }
}
