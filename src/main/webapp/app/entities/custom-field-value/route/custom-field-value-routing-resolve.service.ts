import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomFieldValue, CustomFieldValue } from '../custom-field-value.model';
import { CustomFieldValueService } from '../service/custom-field-value.service';

@Injectable({ providedIn: 'root' })
export class CustomFieldValueRoutingResolveService implements Resolve<ICustomFieldValue> {
  constructor(protected service: CustomFieldValueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomFieldValue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customFieldValue: HttpResponse<CustomFieldValue>) => {
          if (customFieldValue.body) {
            return of(customFieldValue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CustomFieldValue());
  }
}
