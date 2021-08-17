import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { CatalogScope } from 'app/entities/enumerations/catalog-scope.model';
import { ICatalogUnit, CatalogUnit } from '../catalog-unit.model';

import { CatalogUnitService } from './catalog-unit.service';

describe('Service Tests', () => {
  describe('CatalogUnit Service', () => {
    let service: CatalogUnitService;
    let httpMock: HttpTestingController;
    let elemDefault: ICatalogUnit;
    let expectedResult: ICatalogUnit | ICatalogUnit[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CatalogUnitService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        name: 'AAAAAAA',
        nameDe: 'AAAAAAA',
        nameEn: 'AAAAAAA',
        nameFr: 'AAAAAAA',
        nameIt: 'AAAAAAA',
        scope: CatalogScope.SERVICE,
        custom: false,
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

      it('should create a CatalogUnit', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CatalogUnit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CatalogUnit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            name: 'BBBBBB',
            nameDe: 'BBBBBB',
            nameEn: 'BBBBBB',
            nameFr: 'BBBBBB',
            nameIt: 'BBBBBB',
            scope: 'BBBBBB',
            custom: true,
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

      it('should partial update a CatalogUnit', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
            nameDe: 'BBBBBB',
            nameEn: 'BBBBBB',
            inactiv: true,
          },
          new CatalogUnit()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CatalogUnit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            name: 'BBBBBB',
            nameDe: 'BBBBBB',
            nameEn: 'BBBBBB',
            nameFr: 'BBBBBB',
            nameIt: 'BBBBBB',
            scope: 'BBBBBB',
            custom: true,
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

      it('should delete a CatalogUnit', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCatalogUnitToCollectionIfMissing', () => {
        it('should add a CatalogUnit to an empty array', () => {
          const catalogUnit: ICatalogUnit = { id: 123 };
          expectedResult = service.addCatalogUnitToCollectionIfMissing([], catalogUnit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogUnit);
        });

        it('should not add a CatalogUnit to an array that contains it', () => {
          const catalogUnit: ICatalogUnit = { id: 123 };
          const catalogUnitCollection: ICatalogUnit[] = [
            {
              ...catalogUnit,
            },
            { id: 456 },
          ];
          expectedResult = service.addCatalogUnitToCollectionIfMissing(catalogUnitCollection, catalogUnit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CatalogUnit to an array that doesn't contain it", () => {
          const catalogUnit: ICatalogUnit = { id: 123 };
          const catalogUnitCollection: ICatalogUnit[] = [{ id: 456 }];
          expectedResult = service.addCatalogUnitToCollectionIfMissing(catalogUnitCollection, catalogUnit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogUnit);
        });

        it('should add only unique CatalogUnit to an array', () => {
          const catalogUnitArray: ICatalogUnit[] = [{ id: 123 }, { id: 456 }, { id: 1314 }];
          const catalogUnitCollection: ICatalogUnit[] = [{ id: 123 }];
          expectedResult = service.addCatalogUnitToCollectionIfMissing(catalogUnitCollection, ...catalogUnitArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const catalogUnit: ICatalogUnit = { id: 123 };
          const catalogUnit2: ICatalogUnit = { id: 456 };
          expectedResult = service.addCatalogUnitToCollectionIfMissing([], catalogUnit, catalogUnit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogUnit);
          expect(expectedResult).toContain(catalogUnit2);
        });

        it('should accept null and undefined values', () => {
          const catalogUnit: ICatalogUnit = { id: 123 };
          expectedResult = service.addCatalogUnitToCollectionIfMissing([], null, catalogUnit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogUnit);
        });

        it('should return initial array if no CatalogUnit is added', () => {
          const catalogUnitCollection: ICatalogUnit[] = [{ id: 123 }];
          expectedResult = service.addCatalogUnitToCollectionIfMissing(catalogUnitCollection, undefined, null);
          expect(expectedResult).toEqual(catalogUnitCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
