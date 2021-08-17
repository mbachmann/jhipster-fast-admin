import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentInvoiceWorkflow, DocumentInvoiceWorkflow } from '../document-invoice-workflow.model';
import { DocumentInvoiceWorkflowService } from '../service/document-invoice-workflow.service';

@Injectable({ providedIn: 'root' })
export class DocumentInvoiceWorkflowRoutingResolveService implements Resolve<IDocumentInvoiceWorkflow> {
  constructor(protected service: DocumentInvoiceWorkflowService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentInvoiceWorkflow> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentInvoiceWorkflow: HttpResponse<DocumentInvoiceWorkflow>) => {
          if (documentInvoiceWorkflow.body) {
            return of(documentInvoiceWorkflow.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentInvoiceWorkflow());
  }
}
