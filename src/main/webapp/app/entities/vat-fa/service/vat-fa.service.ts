import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVatFa, getVatFaIdentifier } from '../vat-fa.model';

export type EntityResponseType = HttpResponse<IVatFa>;
export type EntityArrayResponseType = HttpResponse<IVatFa[]>;

@Injectable({ providedIn: 'root' })
export class VatFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vat: IVatFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vat);
    return this.http
      .post<IVatFa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vat: IVatFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vat);
    return this.http
      .put<IVatFa>(`${this.resourceUrl}/${getVatFaIdentifier(vat) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vat: IVatFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vat);
    return this.http
      .patch<IVatFa>(`${this.resourceUrl}/${getVatFaIdentifier(vat) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVatFa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVatFa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVatFaToCollectionIfMissing(vatCollection: IVatFa[], ...vatsToCheck: (IVatFa | null | undefined)[]): IVatFa[] {
    const vats: IVatFa[] = vatsToCheck.filter(isPresent);
    if (vats.length > 0) {
      const vatCollectionIdentifiers = vatCollection.map(vatItem => getVatFaIdentifier(vatItem)!);
      const vatsToAdd = vats.filter(vatItem => {
        const vatIdentifier = getVatFaIdentifier(vatItem);
        if (vatIdentifier == null || vatCollectionIdentifiers.includes(vatIdentifier)) {
          return false;
        }
        vatCollectionIdentifiers.push(vatIdentifier);
        return true;
      });
      return [...vatsToAdd, ...vatCollection];
    }
    return vatCollection;
  }

  protected convertDateFromClient(vat: IVatFa): IVatFa {
    return Object.assign({}, vat, {
      validFrom: vat.validFrom?.isValid() ? vat.validFrom.format(DATE_FORMAT) : undefined,
      validUntil: vat.validUntil?.isValid() ? vat.validUntil.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.validFrom = res.body.validFrom ? dayjs(res.body.validFrom) : undefined;
      res.body.validUntil = res.body.validUntil ? dayjs(res.body.validUntil) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vat: IVatFa) => {
        vat.validFrom = vat.validFrom ? dayjs(vat.validFrom) : undefined;
        vat.validUntil = vat.validUntil ? dayjs(vat.validUntil) : undefined;
      });
    }
    return res;
  }
}
