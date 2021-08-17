import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { VatType } from 'app/entities/enumerations/vat-type.model';
import { IValueAddedTax, ValueAddedTax } from '../value-added-tax.model';

import { ValueAddedTaxService } from './value-added-tax.service';

describe('Service Tests', () => {
  describe('ValueAddedTax Service', () => {
    let service: ValueAddedTaxService;
    let httpMock: HttpTestingController;
    let elemDefault: IValueAddedTax;
    let expectedResult: IValueAddedTax | IValueAddedTax[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ValueAddedTaxService);
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

      it('should create a ValueAddedTax', () => {
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

        service.create(new ValueAddedTax()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ValueAddedTax', () => {
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

      it('should partial update a ValueAddedTax', () => {
        const patchObject = Object.assign(
          {
            vatType: 'BBBBBB',
            newVatId: 1,
          },
          new ValueAddedTax()
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

      it('should return a list of ValueAddedTax', () => {
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

      it('should delete a ValueAddedTax', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addValueAddedTaxToCollectionIfMissing', () => {
        it('should add a ValueAddedTax to an empty array', () => {
          const valueAddedTax: IValueAddedTax = { id: 123 };
          expectedResult = service.addValueAddedTaxToCollectionIfMissing([], valueAddedTax);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(valueAddedTax);
        });

        it('should not add a ValueAddedTax to an array that contains it', () => {
          const valueAddedTax: IValueAddedTax = { id: 123 };
          const valueAddedTaxCollection: IValueAddedTax[] = [
            {
              ...valueAddedTax,
            },
            { id: 456 },
          ];
          expectedResult = service.addValueAddedTaxToCollectionIfMissing(valueAddedTaxCollection, valueAddedTax);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ValueAddedTax to an array that doesn't contain it", () => {
          const valueAddedTax: IValueAddedTax = { id: 123 };
          const valueAddedTaxCollection: IValueAddedTax[] = [{ id: 456 }];
          expectedResult = service.addValueAddedTaxToCollectionIfMissing(valueAddedTaxCollection, valueAddedTax);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(valueAddedTax);
        });

        it('should add only unique ValueAddedTax to an array', () => {
          const valueAddedTaxArray: IValueAddedTax[] = [{ id: 123 }, { id: 456 }, { id: 20010 }];
          const valueAddedTaxCollection: IValueAddedTax[] = [{ id: 123 }];
          expectedResult = service.addValueAddedTaxToCollectionIfMissing(valueAddedTaxCollection, ...valueAddedTaxArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const valueAddedTax: IValueAddedTax = { id: 123 };
          const valueAddedTax2: IValueAddedTax = { id: 456 };
          expectedResult = service.addValueAddedTaxToCollectionIfMissing([], valueAddedTax, valueAddedTax2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(valueAddedTax);
          expect(expectedResult).toContain(valueAddedTax2);
        });

        it('should accept null and undefined values', () => {
          const valueAddedTax: IValueAddedTax = { id: 123 };
          expectedResult = service.addValueAddedTaxToCollectionIfMissing([], null, valueAddedTax, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(valueAddedTax);
        });

        it('should return initial array if no ValueAddedTax is added', () => {
          const valueAddedTaxCollection: IValueAddedTax[] = [{ id: 123 }];
          expectedResult = service.addValueAddedTaxToCollectionIfMissing(valueAddedTaxCollection, undefined, null);
          expect(expectedResult).toEqual(valueAddedTaxCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
