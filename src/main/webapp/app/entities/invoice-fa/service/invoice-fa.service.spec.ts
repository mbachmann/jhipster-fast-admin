import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Currency } from 'app/entities/enumerations/currency.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';
import { IsrPosition } from 'app/entities/enumerations/isr-position.model';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { InvoiceStatus } from 'app/entities/enumerations/invoice-status.model';
import { IInvoiceFa, InvoiceFa } from '../invoice-fa.model';

import { InvoiceFaService } from './invoice-fa.service';

describe('Service Tests', () => {
  describe('InvoiceFa Service', () => {
    let service: InvoiceFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IInvoiceFa;
    let expectedResult: IInvoiceFa | IInvoiceFa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InvoiceFaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        number: 'AAAAAAA',
        contactName: 'AAAAAAA',
        date: currentDate,
        due: currentDate,
        periodFrom: currentDate,
        periodTo: currentDate,
        periodText: 'AAAAAAA',
        currency: Currency.AED,
        total: 0,
        vatIncluded: false,
        discountRate: 0,
        discountType: DiscountType.IN_PERCENT,
        cashDiscountRate: 0,
        cashDiscountDate: currentDate,
        totalPaid: 0,
        paidDate: 'AAAAAAA',
        isrPosition: IsrPosition.ADDITIONAL_PAGE,
        isrReferenceNumber: 'AAAAAAA',
        paymentLinkPaypal: false,
        paymentLinkPaypalUrl: 'AAAAAAA',
        paymentLinkPostfinance: false,
        paymentLinkPostfinanceUrl: 'AAAAAAA',
        paymentLinkPayrexx: false,
        paymentLinkPayrexxUrl: 'AAAAAAA',
        paymentLinkSmartcommerce: false,
        paymentLinkSmartcommerceUrl: 'AAAAAAA',
        language: DocumentLanguage.FRENCH,
        pageAmount: 0,
        notes: 'AAAAAAA',
        status: InvoiceStatus.DRAFT,
        created: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            due: currentDate.format(DATE_FORMAT),
            periodFrom: currentDate.format(DATE_FORMAT),
            periodTo: currentDate.format(DATE_FORMAT),
            cashDiscountDate: currentDate.format(DATE_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a InvoiceFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
            due: currentDate.format(DATE_FORMAT),
            periodFrom: currentDate.format(DATE_FORMAT),
            periodTo: currentDate.format(DATE_FORMAT),
            cashDiscountDate: currentDate.format(DATE_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            due: currentDate,
            periodFrom: currentDate,
            periodTo: currentDate,
            cashDiscountDate: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.create(new InvoiceFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a InvoiceFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            contactName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            due: currentDate.format(DATE_FORMAT),
            periodFrom: currentDate.format(DATE_FORMAT),
            periodTo: currentDate.format(DATE_FORMAT),
            periodText: 'BBBBBB',
            currency: 'BBBBBB',
            total: 1,
            vatIncluded: true,
            discountRate: 1,
            discountType: 'BBBBBB',
            cashDiscountRate: 1,
            cashDiscountDate: currentDate.format(DATE_FORMAT),
            totalPaid: 1,
            paidDate: 'BBBBBB',
            isrPosition: 'BBBBBB',
            isrReferenceNumber: 'BBBBBB',
            paymentLinkPaypal: true,
            paymentLinkPaypalUrl: 'BBBBBB',
            paymentLinkPostfinance: true,
            paymentLinkPostfinanceUrl: 'BBBBBB',
            paymentLinkPayrexx: true,
            paymentLinkPayrexxUrl: 'BBBBBB',
            paymentLinkSmartcommerce: true,
            paymentLinkSmartcommerceUrl: 'BBBBBB',
            language: 'BBBBBB',
            pageAmount: 1,
            notes: 'BBBBBB',
            status: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            due: currentDate,
            periodFrom: currentDate,
            periodTo: currentDate,
            cashDiscountDate: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a InvoiceFa', () => {
        const patchObject = Object.assign(
          {
            number: 'BBBBBB',
            contactName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            periodFrom: currentDate.format(DATE_FORMAT),
            periodText: 'BBBBBB',
            total: 1,
            discountRate: 1,
            totalPaid: 1,
            isrPosition: 'BBBBBB',
            paymentLinkPaypal: true,
            paymentLinkPaypalUrl: 'BBBBBB',
            paymentLinkPostfinanceUrl: 'BBBBBB',
            status: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          new InvoiceFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
            due: currentDate,
            periodFrom: currentDate,
            periodTo: currentDate,
            cashDiscountDate: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of InvoiceFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            contactName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            due: currentDate.format(DATE_FORMAT),
            periodFrom: currentDate.format(DATE_FORMAT),
            periodTo: currentDate.format(DATE_FORMAT),
            periodText: 'BBBBBB',
            currency: 'BBBBBB',
            total: 1,
            vatIncluded: true,
            discountRate: 1,
            discountType: 'BBBBBB',
            cashDiscountRate: 1,
            cashDiscountDate: currentDate.format(DATE_FORMAT),
            totalPaid: 1,
            paidDate: 'BBBBBB',
            isrPosition: 'BBBBBB',
            isrReferenceNumber: 'BBBBBB',
            paymentLinkPaypal: true,
            paymentLinkPaypalUrl: 'BBBBBB',
            paymentLinkPostfinance: true,
            paymentLinkPostfinanceUrl: 'BBBBBB',
            paymentLinkPayrexx: true,
            paymentLinkPayrexxUrl: 'BBBBBB',
            paymentLinkSmartcommerce: true,
            paymentLinkSmartcommerceUrl: 'BBBBBB',
            language: 'BBBBBB',
            pageAmount: 1,
            notes: 'BBBBBB',
            status: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            due: currentDate,
            periodFrom: currentDate,
            periodTo: currentDate,
            cashDiscountDate: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a InvoiceFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInvoiceFaToCollectionIfMissing', () => {
        it('should add a InvoiceFa to an empty array', () => {
          const invoice: IInvoiceFa = { id: 123 };
          expectedResult = service.addInvoiceFaToCollectionIfMissing([], invoice);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(invoice);
        });

        it('should not add a InvoiceFa to an array that contains it', () => {
          const invoice: IInvoiceFa = { id: 123 };
          const invoiceCollection: IInvoiceFa[] = [
            {
              ...invoice,
            },
            { id: 456 },
          ];
          expectedResult = service.addInvoiceFaToCollectionIfMissing(invoiceCollection, invoice);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a InvoiceFa to an array that doesn't contain it", () => {
          const invoice: IInvoiceFa = { id: 123 };
          const invoiceCollection: IInvoiceFa[] = [{ id: 456 }];
          expectedResult = service.addInvoiceFaToCollectionIfMissing(invoiceCollection, invoice);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(invoice);
        });

        it('should add only unique InvoiceFa to an array', () => {
          const invoiceArray: IInvoiceFa[] = [{ id: 123 }, { id: 456 }, { id: 87982 }];
          const invoiceCollection: IInvoiceFa[] = [{ id: 123 }];
          expectedResult = service.addInvoiceFaToCollectionIfMissing(invoiceCollection, ...invoiceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const invoice: IInvoiceFa = { id: 123 };
          const invoice2: IInvoiceFa = { id: 456 };
          expectedResult = service.addInvoiceFaToCollectionIfMissing([], invoice, invoice2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(invoice);
          expect(expectedResult).toContain(invoice2);
        });

        it('should accept null and undefined values', () => {
          const invoice: IInvoiceFa = { id: 123 };
          expectedResult = service.addInvoiceFaToCollectionIfMissing([], null, invoice, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(invoice);
        });

        it('should return initial array if no InvoiceFa is added', () => {
          const invoiceCollection: IInvoiceFa[] = [{ id: 123 }];
          expectedResult = service.addInvoiceFaToCollectionIfMissing(invoiceCollection, undefined, null);
          expect(expectedResult).toEqual(invoiceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
