import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICatalogProduct, CatalogProduct } from '../catalog-product.model';

import { CatalogProductService } from './catalog-product.service';

describe('Service Tests', () => {
  describe('CatalogProduct Service', () => {
    let service: CatalogProductService;
    let httpMock: HttpTestingController;
    let elemDefault: ICatalogProduct;
    let expectedResult: ICatalogProduct | ICatalogProduct[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CatalogProductService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        number: 'AAAAAAA',
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        notes: 'AAAAAAA',
        categoryName: 'AAAAAAA',
        includingVat: false,
        vat: 0,
        unitName: 'AAAAAAA',
        price: 0,
        pricePurchase: 0,
        inventoryAvailable: 0,
        inventoryReserved: 0,
        defaultAmount: 0,
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

      it('should create a CatalogProduct', () => {
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

        service.create(new CatalogProduct()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CatalogProduct', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            notes: 'BBBBBB',
            categoryName: 'BBBBBB',
            includingVat: true,
            vat: 1,
            unitName: 'BBBBBB',
            price: 1,
            pricePurchase: 1,
            inventoryAvailable: 1,
            inventoryReserved: 1,
            defaultAmount: 1,
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

      it('should partial update a CatalogProduct', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
            number: 'BBBBBB',
            categoryName: 'BBBBBB',
            vat: 1,
            unitName: 'BBBBBB',
            pricePurchase: 1,
            inventoryAvailable: 1,
            defaultAmount: 1,
            inactiv: true,
          },
          new CatalogProduct()
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

      it('should return a list of CatalogProduct', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            notes: 'BBBBBB',
            categoryName: 'BBBBBB',
            includingVat: true,
            vat: 1,
            unitName: 'BBBBBB',
            price: 1,
            pricePurchase: 1,
            inventoryAvailable: 1,
            inventoryReserved: 1,
            defaultAmount: 1,
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

      it('should delete a CatalogProduct', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCatalogProductToCollectionIfMissing', () => {
        it('should add a CatalogProduct to an empty array', () => {
          const catalogProduct: ICatalogProduct = { id: 123 };
          expectedResult = service.addCatalogProductToCollectionIfMissing([], catalogProduct);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogProduct);
        });

        it('should not add a CatalogProduct to an array that contains it', () => {
          const catalogProduct: ICatalogProduct = { id: 123 };
          const catalogProductCollection: ICatalogProduct[] = [
            {
              ...catalogProduct,
            },
            { id: 456 },
          ];
          expectedResult = service.addCatalogProductToCollectionIfMissing(catalogProductCollection, catalogProduct);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CatalogProduct to an array that doesn't contain it", () => {
          const catalogProduct: ICatalogProduct = { id: 123 };
          const catalogProductCollection: ICatalogProduct[] = [{ id: 456 }];
          expectedResult = service.addCatalogProductToCollectionIfMissing(catalogProductCollection, catalogProduct);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogProduct);
        });

        it('should add only unique CatalogProduct to an array', () => {
          const catalogProductArray: ICatalogProduct[] = [{ id: 123 }, { id: 456 }, { id: 22579 }];
          const catalogProductCollection: ICatalogProduct[] = [{ id: 123 }];
          expectedResult = service.addCatalogProductToCollectionIfMissing(catalogProductCollection, ...catalogProductArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const catalogProduct: ICatalogProduct = { id: 123 };
          const catalogProduct2: ICatalogProduct = { id: 456 };
          expectedResult = service.addCatalogProductToCollectionIfMissing([], catalogProduct, catalogProduct2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogProduct);
          expect(expectedResult).toContain(catalogProduct2);
        });

        it('should accept null and undefined values', () => {
          const catalogProduct: ICatalogProduct = { id: 123 };
          expectedResult = service.addCatalogProductToCollectionIfMissing([], null, catalogProduct, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogProduct);
        });

        it('should return initial array if no CatalogProduct is added', () => {
          const catalogProductCollection: ICatalogProduct[] = [{ id: 123 }];
          expectedResult = service.addCatalogProductToCollectionIfMissing(catalogProductCollection, undefined, null);
          expect(expectedResult).toEqual(catalogProductCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
