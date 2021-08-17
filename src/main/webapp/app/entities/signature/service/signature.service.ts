import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISignature, getSignatureIdentifier } from '../signature.model';

export type EntityResponseType = HttpResponse<ISignature>;
export type EntityArrayResponseType = HttpResponse<ISignature[]>;

@Injectable({ providedIn: 'root' })
export class SignatureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/signatures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(signature: ISignature): Observable<EntityResponseType> {
    return this.http.post<ISignature>(this.resourceUrl, signature, { observe: 'response' });
  }

  update(signature: ISignature): Observable<EntityResponseType> {
    return this.http.put<ISignature>(`${this.resourceUrl}/${getSignatureIdentifier(signature) as number}`, signature, {
      observe: 'response',
    });
  }

  partialUpdate(signature: ISignature): Observable<EntityResponseType> {
    return this.http.patch<ISignature>(`${this.resourceUrl}/${getSignatureIdentifier(signature) as number}`, signature, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISignature>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISignature[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSignatureToCollectionIfMissing(
    signatureCollection: ISignature[],
    ...signaturesToCheck: (ISignature | null | undefined)[]
  ): ISignature[] {
    const signatures: ISignature[] = signaturesToCheck.filter(isPresent);
    if (signatures.length > 0) {
      const signatureCollectionIdentifiers = signatureCollection.map(signatureItem => getSignatureIdentifier(signatureItem)!);
      const signaturesToAdd = signatures.filter(signatureItem => {
        const signatureIdentifier = getSignatureIdentifier(signatureItem);
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
