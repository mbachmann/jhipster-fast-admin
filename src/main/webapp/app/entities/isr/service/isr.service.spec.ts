import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IsrType } from 'app/entities/enumerations/isr-type.model';
import { IsrPosition } from 'app/entities/enumerations/isr-position.model';
import { IIsr, Isr } from '../isr.model';

import { IsrService } from './isr.service';

describe('Service Tests', () => {
  describe('Isr Service', () => {
    let service: IsrService;
    let httpMock: HttpTestingController;
    let elemDefault: IIsr;
    let expectedResult: IIsr | IIsr[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IsrService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        defaultIsr: false,
        type: IsrType.RED_INPAYMENT,
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

      it('should create a Isr', () => {
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

        service.create(new Isr()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Isr', () => {
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

      it('should partial update a Isr', () => {
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
          new Isr()
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

      it('should return a list of Isr', () => {
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

      it('should delete a Isr', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIsrToCollectionIfMissing', () => {
        it('should add a Isr to an empty array', () => {
          const isr: IIsr = { id: 123 };
          expectedResult = service.addIsrToCollectionIfMissing([], isr);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(isr);
        });

        it('should not add a Isr to an array that contains it', () => {
          const isr: IIsr = { id: 123 };
          const isrCollection: IIsr[] = [
            {
              ...isr,
            },
            { id: 456 },
          ];
          expectedResult = service.addIsrToCollectionIfMissing(isrCollection, isr);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Isr to an array that doesn't contain it", () => {
          const isr: IIsr = { id: 123 };
          const isrCollection: IIsr[] = [{ id: 456 }];
          expectedResult = service.addIsrToCollectionIfMissing(isrCollection, isr);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(isr);
        });

        it('should add only unique Isr to an array', () => {
          const isrArray: IIsr[] = [{ id: 123 }, { id: 456 }, { id: 23627 }];
          const isrCollection: IIsr[] = [{ id: 123 }];
          expectedResult = service.addIsrToCollectionIfMissing(isrCollection, ...isrArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const isr: IIsr = { id: 123 };
          const isr2: IIsr = { id: 456 };
          expectedResult = service.addIsrToCollectionIfMissing([], isr, isr2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(isr);
          expect(expectedResult).toContain(isr2);
        });

        it('should accept null and undefined values', () => {
          const isr: IIsr = { id: 123 };
          expectedResult = service.addIsrToCollectionIfMissing([], null, isr, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(isr);
        });

        it('should return initial array if no Isr is added', () => {
          const isrCollection: IIsr[] = [{ id: 123 }];
          expectedResult = service.addIsrToCollectionIfMissing(isrCollection, undefined, null);
          expect(expectedResult).toEqual(isrCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
