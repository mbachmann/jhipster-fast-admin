import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICatalogCategoryFa, CatalogCategoryFa } from '../catalog-category-fa.model';

import { CatalogCategoryFaService } from './catalog-category-fa.service';

describe('Service Tests', () => {
  describe('CatalogCategoryFa Service', () => {
    let service: CatalogCategoryFaService;
    let httpMock: HttpTestingController;
    let elemDefault: ICatalogCategoryFa;
    let expectedResult: ICatalogCategoryFa | ICatalogCategoryFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CatalogCategoryFaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        name: 'AAAAAAA',
        accountingAccountNumber: 'AAAAAAA',
        usage: 0,
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

      it('should create a CatalogCategoryFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CatalogCategoryFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CatalogCategoryFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            name: 'BBBBBB',
            accountingAccountNumber: 'BBBBBB',
            usage: 1,
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

      it('should partial update a CatalogCategoryFa', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
            accountingAccountNumber: 'BBBBBB',
            usage: 1,
          },
          new CatalogCategoryFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CatalogCategoryFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            name: 'BBBBBB',
            accountingAccountNumber: 'BBBBBB',
            usage: 1,
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

      it('should delete a CatalogCategoryFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCatalogCategoryFaToCollectionIfMissing', () => {
        it('should add a CatalogCategoryFa to an empty array', () => {
          const catalogCategory: ICatalogCategoryFa = { id: 123 };
          expectedResult = service.addCatalogCategoryFaToCollectionIfMissing([], catalogCategory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogCategory);
        });

        it('should not add a CatalogCategoryFa to an array that contains it', () => {
          const catalogCategory: ICatalogCategoryFa = { id: 123 };
          const catalogCategoryCollection: ICatalogCategoryFa[] = [
            {
              ...catalogCategory,
            },
            { id: 456 },
          ];
          expectedResult = service.addCatalogCategoryFaToCollectionIfMissing(catalogCategoryCollection, catalogCategory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CatalogCategoryFa to an array that doesn't contain it", () => {
          const catalogCategory: ICatalogCategoryFa = { id: 123 };
          const catalogCategoryCollection: ICatalogCategoryFa[] = [{ id: 456 }];
          expectedResult = service.addCatalogCategoryFaToCollectionIfMissing(catalogCategoryCollection, catalogCategory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogCategory);
        });

        it('should add only unique CatalogCategoryFa to an array', () => {
          const catalogCategoryArray: ICatalogCategoryFa[] = [{ id: 123 }, { id: 456 }, { id: 55726 }];
          const catalogCategoryCollection: ICatalogCategoryFa[] = [{ id: 123 }];
          expectedResult = service.addCatalogCategoryFaToCollectionIfMissing(catalogCategoryCollection, ...catalogCategoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const catalogCategory: ICatalogCategoryFa = { id: 123 };
          const catalogCategory2: ICatalogCategoryFa = { id: 456 };
          expectedResult = service.addCatalogCategoryFaToCollectionIfMissing([], catalogCategory, catalogCategory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogCategory);
          expect(expectedResult).toContain(catalogCategory2);
        });

        it('should accept null and undefined values', () => {
          const catalogCategory: ICatalogCategoryFa = { id: 123 };
          expectedResult = service.addCatalogCategoryFaToCollectionIfMissing([], null, catalogCategory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogCategory);
        });

        it('should return initial array if no CatalogCategoryFa is added', () => {
          const catalogCategoryCollection: ICatalogCategoryFa[] = [{ id: 123 }];
          expectedResult = service.addCatalogCategoryFaToCollectionIfMissing(catalogCategoryCollection, undefined, null);
          expect(expectedResult).toEqual(catalogCategoryCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
