import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Currency } from 'app/entities/enumerations/currency.model';
import { IExchangeRate, ExchangeRate } from '../exchange-rate.model';

import { ExchangeRateService } from './exchange-rate.service';

describe('Service Tests', () => {
  describe('ExchangeRate Service', () => {
    let service: ExchangeRateService;
    let httpMock: HttpTestingController;
    let elemDefault: IExchangeRate;
    let expectedResult: IExchangeRate | IExchangeRate[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExchangeRateService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        currencyFrom: Currency.AED,
        currencyTo: Currency.AED,
        rate: 0,
        created: currentDate,
        inactiv: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ExchangeRate', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
          },
          returnedFromService
        );

        service.create(new ExchangeRate()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExchangeRate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            currencyFrom: 'BBBBBB',
            currencyTo: 'BBBBBB',
            rate: 1,
            created: currentDate.format(DATE_TIME_FORMAT),
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ExchangeRate', () => {
        const patchObject = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          new ExchangeRate()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            created: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ExchangeRate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            currencyFrom: 'BBBBBB',
            currencyTo: 'BBBBBB',
            rate: 1,
            created: currentDate.format(DATE_TIME_FORMAT),
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should delete a ExchangeRate', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExchangeRateToCollectionIfMissing', () => {
        it('should add a ExchangeRate to an empty array', () => {
          const exchangeRate: IExchangeRate = { id: 123 };
          expectedResult = service.addExchangeRateToCollectionIfMissing([], exchangeRate);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exchangeRate);
        });

        it('should not add a ExchangeRate to an array that contains it', () => {
          const exchangeRate: IExchangeRate = { id: 123 };
          const exchangeRateCollection: IExchangeRate[] = [
            {
              ...exchangeRate,
            },
            { id: 456 },
          ];
          expectedResult = service.addExchangeRateToCollectionIfMissing(exchangeRateCollection, exchangeRate);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ExchangeRate to an array that doesn't contain it", () => {
          const exchangeRate: IExchangeRate = { id: 123 };
          const exchangeRateCollection: IExchangeRate[] = [{ id: 456 }];
          expectedResult = service.addExchangeRateToCollectionIfMissing(exchangeRateCollection, exchangeRate);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exchangeRate);
        });

        it('should add only unique ExchangeRate to an array', () => {
          const exchangeRateArray: IExchangeRate[] = [{ id: 123 }, { id: 456 }, { id: 31012 }];
          const exchangeRateCollection: IExchangeRate[] = [{ id: 123 }];
          expectedResult = service.addExchangeRateToCollectionIfMissing(exchangeRateCollection, ...exchangeRateArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const exchangeRate: IExchangeRate = { id: 123 };
          const exchangeRate2: IExchangeRate = { id: 456 };
          expectedResult = service.addExchangeRateToCollectionIfMissing([], exchangeRate, exchangeRate2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exchangeRate);
          expect(expectedResult).toContain(exchangeRate2);
        });

        it('should accept null and undefined values', () => {
          const exchangeRate: IExchangeRate = { id: 123 };
          expectedResult = service.addExchangeRateToCollectionIfMissing([], null, exchangeRate, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exchangeRate);
        });

        it('should return initial array if no ExchangeRate is added', () => {
          const exchangeRateCollection: IExchangeRate[] = [{ id: 123 }];
          expectedResult = service.addExchangeRateToCollectionIfMissing(exchangeRateCollection, undefined, null);
          expect(expectedResult).toEqual(exchangeRateCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
