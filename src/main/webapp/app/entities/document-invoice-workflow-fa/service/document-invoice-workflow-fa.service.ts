import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentInvoiceWorkflowFa, getDocumentInvoiceWorkflowFaIdentifier } from '../document-invoice-workflow-fa.model';

export type EntityResponseType = HttpResponse<IDocumentInvoiceWorkflowFa>;
export type EntityArrayResponseType = HttpResponse<IDocumentInvoiceWorkflowFa[]>;

@Injectable({ providedIn: 'root' })
export class DocumentInvoiceWorkflowFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-invoice-workflows');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentInvoiceWorkflow: IDocumentInvoiceWorkflowFa): Observable<EntityResponseType> {
    return this.http.post<IDocumentInvoiceWorkflowFa>(this.resourceUrl, documentInvoiceWorkflow, { observe: 'response' });
  }

  update(documentInvoiceWorkflow: IDocumentInvoiceWorkflowFa): Observable<EntityResponseType> {
    return this.http.put<IDocumentInvoiceWorkflowFa>(
      `${this.resourceUrl}/${getDocumentInvoiceWorkflowFaIdentifier(documentInvoiceWorkflow) as number}`,
      documentInvoiceWorkflow,
      { observe: 'response' }
    );
  }

  partialUpdate(documentInvoiceWorkflow: IDocumentInvoiceWorkflowFa): Observable<EntityResponseType> {
    return this.http.patch<IDocumentInvoiceWorkflowFa>(
      `${this.resourceUrl}/${getDocumentInvoiceWorkflowFaIdentifier(documentInvoiceWorkflow) as number}`,
      documentInvoiceWorkflow,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentInvoiceWorkflowFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentInvoiceWorkflowFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentInvoiceWorkflowFaToCollectionIfMissing(
    documentInvoiceWorkflowCollection: IDocumentInvoiceWorkflowFa[],
    ...documentInvoiceWorkflowsToCheck: (IDocumentInvoiceWorkflowFa | null | undefined)[]
  ): IDocumentInvoiceWorkflowFa[] {
    const documentInvoiceWorkflows: IDocumentInvoiceWorkflowFa[] = documentInvoiceWorkflowsToCheck.filter(isPresent);
    if (documentInvoiceWorkflows.length > 0) {
      const documentInvoiceWorkflowCollectionIdentifiers = documentInvoiceWorkflowCollection.map(
        documentInvoiceWorkflowItem => getDocumentInvoiceWorkflowFaIdentifier(documentInvoiceWorkflowItem)!
      );
      const documentInvoiceWorkflowsToAdd = documentInvoiceWorkflows.filter(documentInvoiceWorkflowItem => {
        const documentInvoiceWorkflowIdentifier = getDocumentInvoiceWorkflowFaIdentifier(documentInvoiceWorkflowItem);
        if (
          documentInvoiceWorkflowIdentifier == null ||
          documentInvoiceWorkflowCollectionIdentifiers.includes(documentInvoiceWorkflowIdentifier)
        ) {
          return false;
        }
        documentInvoiceWorkflowCollectionIdentifiers.push(documentInvoiceWorkflowIdentifier);
        return true;
      });
      return [...documentInvoiceWorkflowsToAdd, ...documentInvoiceWorkflowCollection];
    }
    return documentInvoiceWorkflowCollection;
  }
}
