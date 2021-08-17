import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { CatalogScope } from 'app/entities/enumerations/catalog-scope.model';
import { ICatalogUnitFa, CatalogUnitFa } from '../catalog-unit-fa.model';

import { CatalogUnitFaService } from './catalog-unit-fa.service';

describe('Service Tests', () => {
  describe('CatalogUnitFa Service', () => {
    let service: CatalogUnitFaService;
    let httpMock: HttpTestingController;
    let elemDefault: ICatalogUnitFa;
    let expectedResult: ICatalogUnitFa | ICatalogUnitFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CatalogUnitFaService);
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

      it('should create a CatalogUnitFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CatalogUnitFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CatalogUnitFa', () => {
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

      it('should partial update a CatalogUnitFa', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
            nameDe: 'BBBBBB',
            nameEn: 'BBBBBB',
            inactiv: true,
          },
          new CatalogUnitFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CatalogUnitFa', () => {
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

      it('should delete a CatalogUnitFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCatalogUnitFaToCollectionIfMissing', () => {
        it('should add a CatalogUnitFa to an empty array', () => {
          const catalogUnit: ICatalogUnitFa = { id: 123 };
          expectedResult = service.addCatalogUnitFaToCollectionIfMissing([], catalogUnit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogUnit);
        });

        it('should not add a CatalogUnitFa to an array that contains it', () => {
          const catalogUnit: ICatalogUnitFa = { id: 123 };
          const catalogUnitCollection: ICatalogUnitFa[] = [
            {
              ...catalogUnit,
            },
            { id: 456 },
          ];
          expectedResult = service.addCatalogUnitFaToCollectionIfMissing(catalogUnitCollection, catalogUnit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CatalogUnitFa to an array that doesn't contain it", () => {
          const catalogUnit: ICatalogUnitFa = { id: 123 };
          const catalogUnitCollection: ICatalogUnitFa[] = [{ id: 456 }];
          expectedResult = service.addCatalogUnitFaToCollectionIfMissing(catalogUnitCollection, catalogUnit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogUnit);
        });

        it('should add only unique CatalogUnitFa to an array', () => {
          const catalogUnitArray: ICatalogUnitFa[] = [{ id: 123 }, { id: 456 }, { id: 1314 }];
          const catalogUnitCollection: ICatalogUnitFa[] = [{ id: 123 }];
          expectedResult = service.addCatalogUnitFaToCollectionIfMissing(catalogUnitCollection, ...catalogUnitArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const catalogUnit: ICatalogUnitFa = { id: 123 };
          const catalogUnit2: ICatalogUnitFa = { id: 456 };
          expectedResult = service.addCatalogUnitFaToCollectionIfMissing([], catalogUnit, catalogUnit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogUnit);
          expect(expectedResult).toContain(catalogUnit2);
        });

        it('should accept null and undefined values', () => {
          const catalogUnit: ICatalogUnitFa = { id: 123 };
          expectedResult = service.addCatalogUnitFaToCollectionIfMissing([], null, catalogUnit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogUnit);
        });

        it('should return initial array if no CatalogUnitFa is added', () => {
          const catalogUnitCollection: ICatalogUnitFa[] = [{ id: 123 }];
          expectedResult = service.addCatalogUnitFaToCollectionIfMissing(catalogUnitCollection, undefined, null);
          expect(expectedResult).toEqual(catalogUnitCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
