import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IsrType } from 'app/entities/enumerations/isr-type.model';
import { IsrPosition } from 'app/entities/enumerations/isr-position.model';
import { IIsrFa, IsrFa } from '../isr-fa.model';

import { IsrFaService } from './isr-fa.service';

describe('Service Tests', () => {
  describe('IsrFa Service', () => {
    let service: IsrFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IIsrFa;
    let expectedResult: IIsrFa | IIsrFa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IsrFaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        defaultIsr: false,
        type: IsrType.RIS,
        position: IsrPosition.ADDITIONAL_PAGE,
        name: 'AAAAAAA',
        bankName: 'AAAAAAA',
        bankAddress: 'AAAAAAA',
        recipientName: 'AAAAAAA',
        recipientAddition: 'AAAAAAA',
        recipientStreet: 'AAAAAAA',
        recipientCity: 'AAAAAAA',
        deliveryNumber: 'AAAAAAA',
        iban: 'AAAAAAA',
        subscriberNumber: 'AAAAAAA',
        leftPrintAdjust: 0,
        topPrintAdjust: 0,
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

      it('should create a IsrFa', () => {
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

        service.create(new IsrFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a IsrFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            defaultIsr: true,
            type: 'BBBBBB',
            position: 'BBBBBB',
            name: 'BBBBBB',
            bankName: 'BBBBBB',
            bankAddress: 'BBBBBB',
            recipientName: 'BBBBBB',
            recipientAddition: 'BBBBBB',
            recipientStreet: 'BBBBBB',
            recipientCity: 'BBBBBB',
            deliveryNumber: 'BBBBBB',
            iban: 'BBBBBB',
            subscriberNumber: 'BBBBBB',
            leftPrintAdjust: 1,
            topPrintAdjust: 1,
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

      it('should partial update a IsrFa', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
            position: 'BBBBBB',
            name: 'BBBBBB',
            bankAddress: 'BBBBBB',
            recipientAddition: 'BBBBBB',
            recipientStreet: 'BBBBBB',
            recipientCity: 'BBBBBB',
            subscriberNumber: 'BBBBBB',
            leftPrintAdjust: 1,
            topPrintAdjust: 1,
          },
          new IsrFa()
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

      it('should return a list of IsrFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            defaultIsr: true,
            type: 'BBBBBB',
            position: 'BBBBBB',
            name: 'BBBBBB',
            bankName: 'BBBBBB',
            bankAddress: 'BBBBBB',
            recipientName: 'BBBBBB',
            recipientAddition: 'BBBBBB',
            recipientStreet: 'BBBBBB',
            recipientCity: 'BBBBBB',
            deliveryNumber: 'BBBBBB',
            iban: 'BBBBBB',
            subscriberNumber: 'BBBBBB',
            leftPrintAdjust: 1,
            topPrintAdjust: 1,
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

      it('should delete a IsrFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIsrFaToCollectionIfMissing', () => {
        it('should add a IsrFa to an empty array', () => {
          const isr: IIsrFa = { id: 123 };
          expectedResult = service.addIsrFaToCollectionIfMissing([], isr);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(isr);
        });

        it('should not add a IsrFa to an array that contains it', () => {
          const isr: IIsrFa = { id: 123 };
          const isrCollection: IIsrFa[] = [
            {
              ...isr,
            },
            { id: 456 },
          ];
          expectedResult = service.addIsrFaToCollectionIfMissing(isrCollection, isr);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a IsrFa to an array that doesn't contain it", () => {
          const isr: IIsrFa = { id: 123 };
          const isrCollection: IIsrFa[] = [{ id: 456 }];
          expectedResult = service.addIsrFaToCollectionIfMissing(isrCollection, isr);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(isr);
        });

        it('should add only unique IsrFa to an array', () => {
          const isrArray: IIsrFa[] = [{ id: 123 }, { id: 456 }, { id: 23627 }];
          const isrCollection: IIsrFa[] = [{ id: 123 }];
          expectedResult = service.addIsrFaToCollectionIfMissing(isrCollection, ...isrArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const isr: IIsrFa = { id: 123 };
          const isr2: IIsrFa = { id: 456 };
          expectedResult = service.addIsrFaToCollectionIfMissing([], isr, isr2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(isr);
          expect(expectedResult).toContain(isr2);
        });

        it('should accept null and undefined values', () => {
          const isr: IIsrFa = { id: 123 };
          expectedResult = service.addIsrFaToCollectionIfMissing([], null, isr, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(isr);
        });

        it('should return initial array if no IsrFa is added', () => {
          const isrCollection: IIsrFa[] = [{ id: 123 }];
          expectedResult = service.addIsrFaToCollectionIfMissing(isrCollection, undefined, null);
          expect(expectedResult).toEqual(isrCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
