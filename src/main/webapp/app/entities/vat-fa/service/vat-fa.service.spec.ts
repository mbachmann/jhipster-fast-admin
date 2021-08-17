import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { VatType } from 'app/entities/enumerations/vat-type.model';
import { IVatFa, VatFa } from '../vat-fa.model';

import { VatFaService } from './vat-fa.service';

describe('Service Tests', () => {
  describe('VatFa Service', () => {
    let service: VatFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IVatFa;
    let expectedResult: IVatFa | IVatFa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VatFaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        vatType: VatType.PERCENT,
        validFrom: currentDate,
        validUntil: currentDate,
        vatPercent: 0,
        inactiv: false,
        newVatId: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            validFrom: currentDate.format(DATE_FORMAT),
            validUntil: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a VatFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            validFrom: currentDate.format(DATE_FORMAT),
            validUntil: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            validFrom: currentDate,
            validUntil: currentDate,
          },
          returnedFromService
        );

        service.create(new VatFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a VatFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            vatType: 'BBBBBB',
            validFrom: currentDate.format(DATE_FORMAT),
            validUntil: currentDate.format(DATE_FORMAT),
            vatPercent: 1,
            inactiv: true,
            newVatId: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            validFrom: currentDate,
            validUntil: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a VatFa', () => {
        const patchObject = Object.assign(
          {
            validFrom: currentDate.format(DATE_FORMAT),
            newVatId: 1,
          },
          new VatFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            validFrom: currentDate,
            validUntil: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of VatFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            vatType: 'BBBBBB',
            validFrom: currentDate.format(DATE_FORMAT),
            validUntil: currentDate.format(DATE_FORMAT),
            vatPercent: 1,
            inactiv: true,
            newVatId: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            validFrom: currentDate,
            validUntil: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a VatFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVatFaToCollectionIfMissing', () => {
        it('should add a VatFa to an empty array', () => {
          const vat: IVatFa = { id: 123 };
          expectedResult = service.addVatFaToCollectionIfMissing([], vat);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vat);
        });

        it('should not add a VatFa to an array that contains it', () => {
          const vat: IVatFa = { id: 123 };
          const vatCollection: IVatFa[] = [
            {
              ...vat,
            },
            { id: 456 },
          ];
          expectedResult = service.addVatFaToCollectionIfMissing(vatCollection, vat);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a VatFa to an array that doesn't contain it", () => {
          const vat: IVatFa = { id: 123 };
          const vatCollection: IVatFa[] = [{ id: 456 }];
          expectedResult = service.addVatFaToCollectionIfMissing(vatCollection, vat);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vat);
        });

        it('should add only unique VatFa to an array', () => {
          const vatArray: IVatFa[] = [{ id: 123 }, { id: 456 }, { id: 34440 }];
          const vatCollection: IVatFa[] = [{ id: 123 }];
          expectedResult = service.addVatFaToCollectionIfMissing(vatCollection, ...vatArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const vat: IVatFa = { id: 123 };
          const vat2: IVatFa = { id: 456 };
          expectedResult = service.addVatFaToCollectionIfMissing([], vat, vat2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vat);
          expect(expectedResult).toContain(vat2);
        });

        it('should accept null and undefined values', () => {
          const vat: IVatFa = { id: 123 };
          expectedResult = service.addVatFaToCollectionIfMissing([], null, vat, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vat);
        });

        it('should return initial array if no VatFa is added', () => {
          const vatCollection: IVatFa[] = [{ id: 123 }];
          expectedResult = service.addVatFaToCollectionIfMissing(vatCollection, undefined, null);
          expect(expectedResult).toEqual(vatCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
