import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDescriptiveDocumentTextFa, DescriptiveDocumentTextFa } from '../descriptive-document-text-fa.model';
import { DescriptiveDocumentTextFaService } from '../service/descriptive-document-text-fa.service';

@Injectable({ providedIn: 'root' })
export class DescriptiveDocumentTextFaRoutingResolveService implements Resolve<IDescriptiveDocumentTextFa> {
  constructor(protected service: DescriptiveDocumentTextFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDescriptiveDocumentTextFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((descriptiveDocumentText: HttpResponse<DescriptiveDocumentTextFa>) => {
          if (descriptiveDocumentText.body) {
            return of(descriptiveDocumentText.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DescriptiveDocumentTextFa());
  }
}
