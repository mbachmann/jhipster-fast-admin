import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvoice, getInvoiceIdentifier } from '../invoice.model';

export type EntityResponseType = HttpResponse<IInvoice>;
export type EntityArrayResponseType = HttpResponse<IInvoice[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .post<IInvoice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .put<IInvoice>(`${this.resourceUrl}/${getInvoiceIdentifier(invoice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .patch<IInvoice>(`${this.resourceUrl}/${getInvoiceIdentifier(invoice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvoiceToCollectionIfMissing(invoiceCollection: IInvoice[], ...invoicesToCheck: (IInvoice | null | undefined)[]): IInvoice[] {
    const invoices: IInvoice[] = invoicesToCheck.filter(isPresent);
    if (invoices.length > 0) {
      const invoiceCollectionIdentifiers = invoiceCollection.map(invoiceItem => getInvoiceIdentifier(invoiceItem)!);
      const invoicesToAdd = invoices.filter(invoiceItem => {
        const invoiceIdentifier = getInvoiceIdentifier(invoiceItem);
        if (invoiceIdentifier == null || invoiceCollectionIdentifiers.includes(invoiceIdentifier)) {
          return false;
        }
        invoiceCollectionIdentifiers.push(invoiceIdentifier);
        return true;
      });
      return [...invoicesToAdd, ...invoiceCollection];
    }
    return invoiceCollection;
  }

  protected convertDateFromClient(invoice: IInvoice): IInvoice {
    return Object.assign({}, invoice, {
      date: invoice.date?.isValid() ? invoice.date.format(DATE_FORMAT) : undefined,
      due: invoice.due?.isValid() ? invoice.due.format(DATE_FORMAT) : undefined,
      periodFrom: invoice.periodFrom?.isValid() ? invoice.periodFrom.format(DATE_FORMAT) : undefined,
      periodTo: invoice.periodTo?.isValid() ? invoice.periodTo.format(DATE_FORMAT) : undefined,
      cashDiscountDate: invoice.cashDiscountDate?.isValid() ? invoice.cashDiscountDate.format(DATE_FORMAT) : undefined,
      created: invoice.created?.isValid() ? invoice.created.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.due = res.body.due ? dayjs(res.body.due) : undefined;
      res.body.periodFrom = res.body.periodFrom ? dayjs(res.body.periodFrom) : undefined;
      res.body.periodTo = res.body.periodTo ? dayjs(res.body.periodTo) : undefined;
      res.body.cashDiscountDate = res.body.cashDiscountDate ? dayjs(res.body.cashDiscountDate) : undefined;
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((invoice: IInvoice) => {
        invoice.date = invoice.date ? dayjs(invoice.date) : undefined;
        invoice.due = invoice.due ? dayjs(invoice.due) : undefined;
        invoice.periodFrom = invoice.periodFrom ? dayjs(invoice.periodFrom) : undefined;
        invoice.periodTo = invoice.periodTo ? dayjs(invoice.periodTo) : undefined;
        invoice.cashDiscountDate = invoice.cashDiscountDate ? dayjs(invoice.cashDiscountDate) : undefined;
        invoice.created = invoice.created ? dayjs(invoice.created) : undefined;
      });
    }
    return res;
  }
}
