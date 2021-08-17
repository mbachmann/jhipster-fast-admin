import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { CostUnitType } from 'app/entities/enumerations/cost-unit-type.model';
import { ICostUnit, CostUnit } from '../cost-unit.model';

import { CostUnitService } from './cost-unit.service';

describe('Service Tests', () => {
  describe('CostUnit Service', () => {
    let service: CostUnitService;
    let httpMock: HttpTestingController;
    let elemDefault: ICostUnit;
    let expectedResult: ICostUnit | ICostUnit[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CostUnitService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        number: 'AAAAAAA',
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        type: CostUnitType.PRODUCTIVE,
        inactiv: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CostUnit', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CostUnit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CostUnit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CostUnit', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            type: 'BBBBBB',
            inactiv: true,
          },
          new CostUnit()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CostUnit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CostUnit', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCostUnitToCollectionIfMissing', () => {
        it('should add a CostUnit to an empty array', () => {
          const costUnit: ICostUnit = { id: 123 };
          expectedResult = service.addCostUnitToCollectionIfMissing([], costUnit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(costUnit);
        });

        it('should not add a CostUnit to an array that contains it', () => {
          const costUnit: ICostUnit = { id: 123 };
          const costUnitCollection: ICostUnit[] = [
            {
              ...costUnit,
            },
            { id: 456 },
          ];
          expectedResult = service.addCostUnitToCollectionIfMissing(costUnitCollection, costUnit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CostUnit to an array that doesn't contain it", () => {
          const costUnit: ICostUnit = { id: 123 };
          const costUnitCollection: ICostUnit[] = [{ id: 456 }];
          expectedResult = service.addCostUnitToCollectionIfMissing(costUnitCollection, costUnit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(costUnit);
        });

        it('should add only unique CostUnit to an array', () => {
          const costUnitArray: ICostUnit[] = [{ id: 123 }, { id: 456 }, { id: 60360 }];
          const costUnitCollection: ICostUnit[] = [{ id: 123 }];
          expectedResult = service.addCostUnitToCollectionIfMissing(costUnitCollection, ...costUnitArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const costUnit: ICostUnit = { id: 123 };
          const costUnit2: ICostUnit = { id: 456 };
          expectedResult = service.addCostUnitToCollectionIfMissing([], costUnit, costUnit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(costUnit);
          expect(expectedResult).toContain(costUnit2);
        });

        it('should accept null and undefined values', () => {
          const costUnit: ICostUnit = { id: 123 };
          expectedResult = service.addCostUnitToCollectionIfMissing([], null, costUnit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(costUnit);
        });

        it('should return initial array if no CostUnit is added', () => {
          const costUnitCollection: ICostUnit[] = [{ id: 123 }];
          expectedResult = service.addCostUnitToCollectionIfMissing(costUnitCollection, undefined, null);
          expect(expectedResult).toEqual(costUnitCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
