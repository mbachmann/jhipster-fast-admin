import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Currency } from 'app/entities/enumerations/currency.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { OrderConfirmationStatus } from 'app/entities/enumerations/order-confirmation-status.model';
import { IOrderConfirmation, OrderConfirmation } from '../order-confirmation.model';

import { OrderConfirmationService } from './order-confirmation.service';

describe('Service Tests', () => {
  describe('OrderConfirmation Service', () => {
    let service: OrderConfirmationService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrderConfirmation;
    let expectedResult: IOrderConfirmation | IOrderConfirmation[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrderConfirmationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        number: 'AAAAAAA',
        contactName: 'AAAAAAA',
        date: currentDate,
        periodText: 'AAAAAAA',
        currency: Currency.AED,
        total: 0,
        vatIncluded: false,
        discountRate: 0,
        discountType: DiscountType.IN_PERCENT,
        language: DocumentLanguage.FRENCH,
        pageAmount: 0,
        notes: 'AAAAAAA',
        status: OrderConfirmationStatus.DRAFT,
        created: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a OrderConfirmation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.create(new OrderConfirmation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrderConfirmation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            contactName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            periodText: 'BBBBBB',
            currency: 'BBBBBB',
            total: 1,
            vatIncluded: true,
            discountRate: 1,
            discountType: 'BBBBBB',
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
            created: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OrderConfirmation', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
            number: 'BBBBBB',
            contactName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            currency: 'BBBBBB',
            discountRate: 1,
            discountType: 'BBBBBB',
            pageAmount: 1,
            notes: 'BBBBBB',
            status: 'BBBBBB',
          },
          new OrderConfirmation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OrderConfirmation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            contactName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            periodText: 'BBBBBB',
            currency: 'BBBBBB',
            total: 1,
            vatIncluded: true,
            discountRate: 1,
            discountType: 'BBBBBB',
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

      it('should delete a OrderConfirmation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrderConfirmationToCollectionIfMissing', () => {
        it('should add a OrderConfirmation to an empty array', () => {
          const orderConfirmation: IOrderConfirmation = { id: 123 };
          expectedResult = service.addOrderConfirmationToCollectionIfMissing([], orderConfirmation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(orderConfirmation);
        });

        it('should not add a OrderConfirmation to an array that contains it', () => {
          const orderConfirmation: IOrderConfirmation = { id: 123 };
          const orderConfirmationCollection: IOrderConfirmation[] = [
            {
              ...orderConfirmation,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrderConfirmationToCollectionIfMissing(orderConfirmationCollection, orderConfirmation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrderConfirmation to an array that doesn't contain it", () => {
          const orderConfirmation: IOrderConfirmation = { id: 123 };
          const orderConfirmationCollection: IOrderConfirmation[] = [{ id: 456 }];
          expectedResult = service.addOrderConfirmationToCollectionIfMissing(orderConfirmationCollection, orderConfirmation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(orderConfirmation);
        });

        it('should add only unique OrderConfirmation to an array', () => {
          const orderConfirmationArray: IOrderConfirmation[] = [{ id: 123 }, { id: 456 }, { id: 67636 }];
          const orderConfirmationCollection: IOrderConfirmation[] = [{ id: 123 }];
          expectedResult = service.addOrderConfirmationToCollectionIfMissing(orderConfirmationCollection, ...orderConfirmationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const orderConfirmation: IOrderConfirmation = { id: 123 };
          const orderConfirmation2: IOrderConfirmation = { id: 456 };
          expectedResult = service.addOrderConfirmationToCollectionIfMissing([], orderConfirmation, orderConfirmation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(orderConfirmation);
          expect(expectedResult).toContain(orderConfirmation2);
        });

        it('should accept null and undefined values', () => {
          const orderConfirmation: IOrderConfirmation = { id: 123 };
          expectedResult = service.addOrderConfirmationToCollectionIfMissing([], null, orderConfirmation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(orderConfirmation);
        });

        it('should return initial array if no OrderConfirmation is added', () => {
          const orderConfirmationCollection: IOrderConfirmation[] = [{ id: 123 }];
          expectedResult = service.addOrderConfirmationToCollectionIfMissing(orderConfirmationCollection, undefined, null);
          expect(expectedResult).toEqual(orderConfirmationCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
