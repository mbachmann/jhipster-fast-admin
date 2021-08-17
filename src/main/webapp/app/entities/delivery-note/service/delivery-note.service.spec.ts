import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Currency } from 'app/entities/enumerations/currency.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { DeliveryNoteStatus } from 'app/entities/enumerations/delivery-note-status.model';
import { IDeliveryNote, DeliveryNote } from '../delivery-note.model';

import { DeliveryNoteService } from './delivery-note.service';

describe('Service Tests', () => {
  describe('DeliveryNote Service', () => {
    let service: DeliveryNoteService;
    let httpMock: HttpTestingController;
    let elemDefault: IDeliveryNote;
    let expectedResult: IDeliveryNote | IDeliveryNote[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DeliveryNoteService);
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
        status: DeliveryNoteStatus.DRAFT,
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

      it('should create a DeliveryNote', () => {
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

        service.create(new DeliveryNote()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DeliveryNote', () => {
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

      it('should partial update a DeliveryNote', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
            contactName: 'BBBBBB',
            periodText: 'BBBBBB',
            total: 1,
            discountRate: 1,
            pageAmount: 1,
            notes: 'BBBBBB',
            status: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          new DeliveryNote()
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

      it('should return a list of DeliveryNote', () => {
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

      it('should delete a DeliveryNote', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDeliveryNoteToCollectionIfMissing', () => {
        it('should add a DeliveryNote to an empty array', () => {
          const deliveryNote: IDeliveryNote = { id: 123 };
          expectedResult = service.addDeliveryNoteToCollectionIfMissing([], deliveryNote);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(deliveryNote);
        });

        it('should not add a DeliveryNote to an array that contains it', () => {
          const deliveryNote: IDeliveryNote = { id: 123 };
          const deliveryNoteCollection: IDeliveryNote[] = [
            {
              ...deliveryNote,
            },
            { id: 456 },
          ];
          expectedResult = service.addDeliveryNoteToCollectionIfMissing(deliveryNoteCollection, deliveryNote);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DeliveryNote to an array that doesn't contain it", () => {
          const deliveryNote: IDeliveryNote = { id: 123 };
          const deliveryNoteCollection: IDeliveryNote[] = [{ id: 456 }];
          expectedResult = service.addDeliveryNoteToCollectionIfMissing(deliveryNoteCollection, deliveryNote);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(deliveryNote);
        });

        it('should add only unique DeliveryNote to an array', () => {
          const deliveryNoteArray: IDeliveryNote[] = [{ id: 123 }, { id: 456 }, { id: 8901 }];
          const deliveryNoteCollection: IDeliveryNote[] = [{ id: 123 }];
          expectedResult = service.addDeliveryNoteToCollectionIfMissing(deliveryNoteCollection, ...deliveryNoteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const deliveryNote: IDeliveryNote = { id: 123 };
          const deliveryNote2: IDeliveryNote = { id: 456 };
          expectedResult = service.addDeliveryNoteToCollectionIfMissing([], deliveryNote, deliveryNote2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(deliveryNote);
          expect(expectedResult).toContain(deliveryNote2);
        });

        it('should accept null and undefined values', () => {
          const deliveryNote: IDeliveryNote = { id: 123 };
          expectedResult = service.addDeliveryNoteToCollectionIfMissing([], null, deliveryNote, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(deliveryNote);
        });

        it('should return initial array if no DeliveryNote is added', () => {
          const deliveryNoteCollection: IDeliveryNote[] = [{ id: 123 }];
          expectedResult = service.addDeliveryNoteToCollectionIfMissing(deliveryNoteCollection, undefined, null);
          expect(expectedResult).toEqual(deliveryNoteCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
