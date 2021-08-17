import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Currency } from 'app/entities/enumerations/currency.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';
import { OfferAcceptOnlineStatus } from 'app/entities/enumerations/offer-accept-online-status.model';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { OfferStatus } from 'app/entities/enumerations/offer-status.model';
import { IOfferFa, OfferFa } from '../offer-fa.model';

import { OfferFaService } from './offer-fa.service';

describe('Service Tests', () => {
  describe('OfferFa Service', () => {
    let service: OfferFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IOfferFa;
    let expectedResult: IOfferFa | IOfferFa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OfferFaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        number: 'AAAAAAA',
        contactName: 'AAAAAAA',
        date: currentDate,
        validUntil: currentDate,
        periodText: 'AAAAAAA',
        currency: Currency.AED,
        total: 0,
        vatIncluded: false,
        discountRate: 0,
        discountType: DiscountType.IN_PERCENT,
        acceptOnline: false,
        acceptOnlineUrl: 'AAAAAAA',
        acceptOnlineStatus: OfferAcceptOnlineStatus.ACCEPTED,
        language: DocumentLanguage.FRENCH,
        pageAmount: 0,
        notes: 'AAAAAAA',
        status: OfferStatus.DRAFT,
        created: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            validUntil: currentDate.format(DATE_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a OfferFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
            validUntil: currentDate.format(DATE_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            validUntil: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.create(new OfferFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OfferFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            contactName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            validUntil: currentDate.format(DATE_FORMAT),
            periodText: 'BBBBBB',
            currency: 'BBBBBB',
            total: 1,
            vatIncluded: true,
            discountRate: 1,
            discountType: 'BBBBBB',
            acceptOnline: true,
            acceptOnlineUrl: 'BBBBBB',
            acceptOnlineStatus: 'BBBBBB',
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
            validUntil: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OfferFa', () => {
        const patchObject = Object.assign(
          {
            number: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            periodText: 'BBBBBB',
            currency: 'BBBBBB',
            total: 1,
            discountType: 'BBBBBB',
            acceptOnline: true,
            acceptOnlineUrl: 'BBBBBB',
            pageAmount: 1,
            notes: 'BBBBBB',
          },
          new OfferFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
            validUntil: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OfferFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            contactName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            validUntil: currentDate.format(DATE_FORMAT),
            periodText: 'BBBBBB',
            currency: 'BBBBBB',
            total: 1,
            vatIncluded: true,
            discountRate: 1,
            discountType: 'BBBBBB',
            acceptOnline: true,
            acceptOnlineUrl: 'BBBBBB',
            acceptOnlineStatus: 'BBBBBB',
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
            validUntil: currentDate,
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

      it('should delete a OfferFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOfferFaToCollectionIfMissing', () => {
        it('should add a OfferFa to an empty array', () => {
          const offer: IOfferFa = { id: 123 };
          expectedResult = service.addOfferFaToCollectionIfMissing([], offer);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(offer);
        });

        it('should not add a OfferFa to an array that contains it', () => {
          const offer: IOfferFa = { id: 123 };
          const offerCollection: IOfferFa[] = [
            {
              ...offer,
            },
            { id: 456 },
          ];
          expectedResult = service.addOfferFaToCollectionIfMissing(offerCollection, offer);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OfferFa to an array that doesn't contain it", () => {
          const offer: IOfferFa = { id: 123 };
          const offerCollection: IOfferFa[] = [{ id: 456 }];
          expectedResult = service.addOfferFaToCollectionIfMissing(offerCollection, offer);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(offer);
        });

        it('should add only unique OfferFa to an array', () => {
          const offerArray: IOfferFa[] = [{ id: 123 }, { id: 456 }, { id: 74647 }];
          const offerCollection: IOfferFa[] = [{ id: 123 }];
          expectedResult = service.addOfferFaToCollectionIfMissing(offerCollection, ...offerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const offer: IOfferFa = { id: 123 };
          const offer2: IOfferFa = { id: 456 };
          expectedResult = service.addOfferFaToCollectionIfMissing([], offer, offer2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(offer);
          expect(expectedResult).toContain(offer2);
        });

        it('should accept null and undefined values', () => {
          const offer: IOfferFa = { id: 123 };
          expectedResult = service.addOfferFaToCollectionIfMissing([], null, offer, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(offer);
        });

        it('should return initial array if no OfferFa is added', () => {
          const offerCollection: IOfferFa[] = [{ id: 123 }];
          expectedResult = service.addOfferFaToCollectionIfMissing(offerCollection, undefined, null);
          expect(expectedResult).toEqual(offerCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
