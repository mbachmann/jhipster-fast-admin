import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomFieldMySuffix, CustomFieldMySuffix } from '../custom-field-my-suffix.model';
import { CustomFieldMySuffixService } from '../service/custom-field-my-suffix.service';

@Injectable({ providedIn: 'root' })
export class CustomFieldMySuffixRoutingResolveService implements Resolve<ICustomFieldMySuffix> {
  constructor(protected service: CustomFieldMySuffixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomFieldMySuffix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customField: HttpResponse<CustomFieldMySuffix>) => {
          if (customField.body) {
            return of(customField.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CustomFieldMySuffix());
  }
}
