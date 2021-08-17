import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentLetterFa, getDocumentLetterFaIdentifier } from '../document-letter-fa.model';

export type EntityResponseType = HttpResponse<IDocumentLetterFa>;
export type EntityArrayResponseType = HttpResponse<IDocumentLetterFa[]>;

@Injectable({ providedIn: 'root' })
export class DocumentLetterFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-letters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentLetter: IDocumentLetterFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentLetter);
    return this.http
      .post<IDocumentLetterFa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(documentLetter: IDocumentLetterFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentLetter);
    return this.http
      .put<IDocumentLetterFa>(`${this.resourceUrl}/${getDocumentLetterFaIdentifier(documentLetter) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(documentLetter: IDocumentLetterFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentLetter);
    return this.http
      .patch<IDocumentLetterFa>(`${this.resourceUrl}/${getDocumentLetterFaIdentifier(documentLetter) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDocumentLetterFa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDocumentLetterFa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentLetterFaToCollectionIfMissing(
    documentLetterCollection: IDocumentLetterFa[],
    ...documentLettersToCheck: (IDocumentLetterFa | null | undefined)[]
  ): IDocumentLetterFa[] {
    const documentLetters: IDocumentLetterFa[] = documentLettersToCheck.filter(isPresent);
    if (documentLetters.length > 0) {
      const documentLetterCollectionIdentifiers = documentLetterCollection.map(
        documentLetterItem => getDocumentLetterFaIdentifier(documentLetterItem)!
      );
      const documentLettersToAdd = documentLetters.filter(documentLetterItem => {
        const documentLetterIdentifier = getDocumentLetterFaIdentifier(documentLetterItem);
        if (documentLetterIdentifier == null || documentLetterCollectionIdentifiers.includes(documentLetterIdentifier)) {
          return false;
        }
        documentLetterCollectionIdentifiers.push(documentLetterIdentifier);
        return true;
      });
      return [...documentLettersToAdd, ...documentLetterCollection];
    }
    return documentLetterCollection;
  }

  protected convertDateFromClient(documentLetter: IDocumentLetterFa): IDocumentLetterFa {
    return Object.assign({}, documentLetter, {
      date: documentLetter.date?.isValid() ? documentLetter.date.format(DATE_FORMAT) : undefined,
      created: documentLetter.created?.isValid() ? documentLetter.created.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((documentLetter: IDocumentLetterFa) => {
        documentLetter.date = documentLetter.date ? dayjs(documentLetter.date) : undefined;
        documentLetter.created = documentLetter.created ? dayjs(documentLetter.created) : undefined;
      });
    }
    return res;
  }
}
