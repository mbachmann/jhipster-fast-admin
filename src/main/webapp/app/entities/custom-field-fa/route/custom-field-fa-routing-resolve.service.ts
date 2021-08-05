import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomFieldFa, CustomFieldFa } from '../custom-field-fa.model';
import { CustomFieldFaService } from '../service/custom-field-fa.service';

@Injectable({ providedIn: 'root' })
export class CustomFieldFaRoutingResolveService implements Resolve<ICustomFieldFa> {
  constructor(protected service: CustomFieldFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomFieldFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customField: HttpResponse<CustomFieldFa>) => {
          if (customField.body) {
            return of(customField.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CustomFieldFa());
  }
}
