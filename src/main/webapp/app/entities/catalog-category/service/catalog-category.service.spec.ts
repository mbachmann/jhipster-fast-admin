import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICatalogCategory, CatalogCategory } from '../catalog-category.model';

import { CatalogCategoryService } from './catalog-category.service';

describe('Service Tests', () => {
  describe('CatalogCategory Service', () => {
    let service: CatalogCategoryService;
    let httpMock: HttpTestingController;
    let elemDefault: ICatalogCategory;
    let expectedResult: ICatalogCategory | ICatalogCategory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CatalogCategoryService);
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

      it('should create a CatalogCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CatalogCategory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CatalogCategory', () => {
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

      it('should partial update a CatalogCategory', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
            accountingAccountNumber: 'BBBBBB',
            usage: 1,
          },
          new CatalogCategory()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CatalogCategory', () => {
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

      it('should delete a CatalogCategory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCatalogCategoryToCollectionIfMissing', () => {
        it('should add a CatalogCategory to an empty array', () => {
          const catalogCategory: ICatalogCategory = { id: 123 };
          expectedResult = service.addCatalogCategoryToCollectionIfMissing([], catalogCategory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogCategory);
        });

        it('should not add a CatalogCategory to an array that contains it', () => {
          const catalogCategory: ICatalogCategory = { id: 123 };
          const catalogCategoryCollection: ICatalogCategory[] = [
            {
              ...catalogCategory,
            },
            { id: 456 },
          ];
          expectedResult = service.addCatalogCategoryToCollectionIfMissing(catalogCategoryCollection, catalogCategory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CatalogCategory to an array that doesn't contain it", () => {
          const catalogCategory: ICatalogCategory = { id: 123 };
          const catalogCategoryCollection: ICatalogCategory[] = [{ id: 456 }];
          expectedResult = service.addCatalogCategoryToCollectionIfMissing(catalogCategoryCollection, catalogCategory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogCategory);
        });

        it('should add only unique CatalogCategory to an array', () => {
          const catalogCategoryArray: ICatalogCategory[] = [{ id: 123 }, { id: 456 }, { id: 55726 }];
          const catalogCategoryCollection: ICatalogCategory[] = [{ id: 123 }];
          expectedResult = service.addCatalogCategoryToCollectionIfMissing(catalogCategoryCollection, ...catalogCategoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const catalogCategory: ICatalogCategory = { id: 123 };
          const catalogCategory2: ICatalogCategory = { id: 456 };
          expectedResult = service.addCatalogCategoryToCollectionIfMissing([], catalogCategory, catalogCategory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogCategory);
          expect(expectedResult).toContain(catalogCategory2);
        });

        it('should accept null and undefined values', () => {
          const catalogCategory: ICatalogCategory = { id: 123 };
          expectedResult = service.addCatalogCategoryToCollectionIfMissing([], null, catalogCategory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogCategory);
        });

        it('should return initial array if no CatalogCategory is added', () => {
          const catalogCategoryCollection: ICatalogCategory[] = [{ id: 123 }];
          expectedResult = service.addCatalogCategoryToCollectionIfMissing(catalogCategoryCollection, undefined, null);
          expect(expectedResult).toEqual(catalogCategoryCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
