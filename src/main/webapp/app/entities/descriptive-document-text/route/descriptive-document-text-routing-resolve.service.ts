import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDescriptiveDocumentText, DescriptiveDocumentText } from '../descriptive-document-text.model';
import { DescriptiveDocumentTextService } from '../service/descriptive-document-text.service';

@Injectable({ providedIn: 'root' })
export class DescriptiveDocumentTextRoutingResolveService implements Resolve<IDescriptiveDocumentText> {
  constructor(protected service: DescriptiveDocumentTextService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDescriptiveDocumentText> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((descriptiveDocumentText: HttpResponse<DescriptiveDocumentText>) => {
          if (descriptiveDocumentText.body) {
            return of(descriptiveDocumentText.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DescriptiveDocumentText());
  }
}
