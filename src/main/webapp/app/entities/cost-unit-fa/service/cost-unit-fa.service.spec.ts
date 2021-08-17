import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { CostUnitType } from 'app/entities/enumerations/cost-unit-type.model';
import { ICostUnitFa, CostUnitFa } from '../cost-unit-fa.model';

import { CostUnitFaService } from './cost-unit-fa.service';

describe('Service Tests', () => {
  describe('CostUnitFa Service', () => {
    let service: CostUnitFaService;
    let httpMock: HttpTestingController;
    let elemDefault: ICostUnitFa;
    let expectedResult: ICostUnitFa | ICostUnitFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CostUnitFaService);
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

      it('should create a CostUnitFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CostUnitFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CostUnitFa', () => {
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

      it('should partial update a CostUnitFa', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            type: 'BBBBBB',
            inactiv: true,
          },
          new CostUnitFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CostUnitFa', () => {
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

      it('should delete a CostUnitFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCostUnitFaToCollectionIfMissing', () => {
        it('should add a CostUnitFa to an empty array', () => {
          const costUnit: ICostUnitFa = { id: 123 };
          expectedResult = service.addCostUnitFaToCollectionIfMissing([], costUnit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(costUnit);
        });

        it('should not add a CostUnitFa to an array that contains it', () => {
          const costUnit: ICostUnitFa = { id: 123 };
          const costUnitCollection: ICostUnitFa[] = [
            {
              ...costUnit,
            },
            { id: 456 },
          ];
          expectedResult = service.addCostUnitFaToCollectionIfMissing(costUnitCollection, costUnit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CostUnitFa to an array that doesn't contain it", () => {
          const costUnit: ICostUnitFa = { id: 123 };
          const costUnitCollection: ICostUnitFa[] = [{ id: 456 }];
          expectedResult = service.addCostUnitFaToCollectionIfMissing(costUnitCollection, costUnit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(costUnit);
        });

        it('should add only unique CostUnitFa to an array', () => {
          const costUnitArray: ICostUnitFa[] = [{ id: 123 }, { id: 456 }, { id: 60360 }];
          const costUnitCollection: ICostUnitFa[] = [{ id: 123 }];
          expectedResult = service.addCostUnitFaToCollectionIfMissing(costUnitCollection, ...costUnitArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const costUnit: ICostUnitFa = { id: 123 };
          const costUnit2: ICostUnitFa = { id: 456 };
          expectedResult = service.addCostUnitFaToCollectionIfMissing([], costUnit, costUnit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(costUnit);
          expect(expectedResult).toContain(costUnit2);
        });

        it('should accept null and undefined values', () => {
          const costUnit: ICostUnitFa = { id: 123 };
          expectedResult = service.addCostUnitFaToCollectionIfMissing([], null, costUnit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(costUnit);
        });

        it('should return initial array if no CostUnitFa is added', () => {
          const costUnitCollection: ICostUnitFa[] = [{ id: 123 }];
          expectedResult = service.addCostUnitFaToCollectionIfMissing(costUnitCollection, undefined, null);
          expect(expectedResult).toEqual(costUnitCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
