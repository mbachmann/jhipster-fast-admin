import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentLetter, getDocumentLetterIdentifier } from '../document-letter.model';

export type EntityResponseType = HttpResponse<IDocumentLetter>;
export type EntityArrayResponseType = HttpResponse<IDocumentLetter[]>;

@Injectable({ providedIn: 'root' })
export class DocumentLetterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-letters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentLetter: IDocumentLetter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentLetter);
    return this.http
      .post<IDocumentLetter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(documentLetter: IDocumentLetter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentLetter);
    return this.http
      .put<IDocumentLetter>(`${this.resourceUrl}/${getDocumentLetterIdentifier(documentLetter) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(documentLetter: IDocumentLetter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentLetter);
    return this.http
      .patch<IDocumentLetter>(`${this.resourceUrl}/${getDocumentLetterIdentifier(documentLetter) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDocumentLetter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDocumentLetter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentLetterToCollectionIfMissing(
    documentLetterCollection: IDocumentLetter[],
    ...documentLettersToCheck: (IDocumentLetter | null | undefined)[]
  ): IDocumentLetter[] {
    const documentLetters: IDocumentLetter[] = documentLettersToCheck.filter(isPresent);
    if (documentLetters.length > 0) {
      const documentLetterCollectionIdentifiers = documentLetterCollection.map(
        documentLetterItem => getDocumentLetterIdentifier(documentLetterItem)!
      );
      const documentLettersToAdd = documentLetters.filter(documentLetterItem => {
        const documentLetterIdentifier = getDocumentLetterIdentifier(documentLetterItem);
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

  protected convertDateFromClient(documentLetter: IDocumentLetter): IDocumentLetter {
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
      res.body.forEach((documentLetter: IDocumentLetter) => {
        documentLetter.date = documentLetter.date ? dayjs(documentLetter.date) : undefined;
        documentLetter.created = documentLetter.created ? dayjs(documentLetter.created) : undefined;
      });
    }
    return res;
  }
}
