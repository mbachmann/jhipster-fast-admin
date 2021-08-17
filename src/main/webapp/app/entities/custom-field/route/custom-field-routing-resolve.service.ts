import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomField, CustomField } from '../custom-field.model';
import { CustomFieldService } from '../service/custom-field.service';

@Injectable({ providedIn: 'root' })
export class CustomFieldRoutingResolveService implements Resolve<ICustomField> {
  constructor(protected service: CustomFieldService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomField> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customField: HttpResponse<CustomField>) => {
          if (customField.body) {
            return of(customField.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CustomField());
  }
}
