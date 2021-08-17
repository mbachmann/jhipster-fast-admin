import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISignatureFa, getSignatureFaIdentifier } from '../signature-fa.model';

export type EntityResponseType = HttpResponse<ISignatureFa>;
export type EntityArrayResponseType = HttpResponse<ISignatureFa[]>;

@Injectable({ providedIn: 'root' })
export class SignatureFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/signatures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(signature: ISignatureFa): Observable<EntityResponseType> {
    return this.http.post<ISignatureFa>(this.resourceUrl, signature, { observe: 'response' });
  }

  update(signature: ISignatureFa): Observable<EntityResponseType> {
    return this.http.put<ISignatureFa>(`${this.resourceUrl}/${getSignatureFaIdentifier(signature) as number}`, signature, {
      observe: 'response',
    });
  }

  partialUpdate(signature: ISignatureFa): Observable<EntityResponseType> {
    return this.http.patch<ISignatureFa>(`${this.resourceUrl}/${getSignatureFaIdentifier(signature) as number}`, signature, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISignatureFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISignatureFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSignatureFaToCollectionIfMissing(
    signatureCollection: ISignatureFa[],
    ...signaturesToCheck: (ISignatureFa | null | undefined)[]
  ): ISignatureFa[] {
    const signatures: ISignatureFa[] = signaturesToCheck.filter(isPresent);
    if (signatures.length > 0) {
      const signatureCollectionIdentifiers = signatureCollection.map(signatureItem => getSignatureFaIdentifier(signatureItem)!);
      const signaturesToAdd = signatures.filter(signatureItem => {
        const signatureIdentifier = getSignatureFaIdentifier(signatureItem);
        if (signatureIdentifier == null || signatureCollectionIdentifiers.includes(signatureIdentifier)) {
          return false;
        }
        signatureCollectionIdentifiers.push(signatureIdentifier);
        return true;
      });
      return [...signaturesToAdd, ...signatureCollection];
    }
    return signatureCollection;
  }
}
