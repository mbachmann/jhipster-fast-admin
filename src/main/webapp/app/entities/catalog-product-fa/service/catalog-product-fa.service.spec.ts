import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICatalogProductFa, CatalogProductFa } from '../catalog-product-fa.model';

import { CatalogProductFaService } from './catalog-product-fa.service';

describe('Service Tests', () => {
  describe('CatalogProductFa Service', () => {
    let service: CatalogProductFaService;
    let httpMock: HttpTestingController;
    let elemDefault: ICatalogProductFa;
    let expectedResult: ICatalogProductFa | ICatalogProductFa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CatalogProductFaService);
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

      it('should create a CatalogProductFa', () => {
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

        service.create(new CatalogProductFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CatalogProductFa', () => {
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

      it('should partial update a CatalogProductFa', () => {
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
          new CatalogProductFa()
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

      it('should return a list of CatalogProductFa', () => {
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

      it('should delete a CatalogProductFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCatalogProductFaToCollectionIfMissing', () => {
        it('should add a CatalogProductFa to an empty array', () => {
          const catalogProduct: ICatalogProductFa = { id: 123 };
          expectedResult = service.addCatalogProductFaToCollectionIfMissing([], catalogProduct);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogProduct);
        });

        it('should not add a CatalogProductFa to an array that contains it', () => {
          const catalogProduct: ICatalogProductFa = { id: 123 };
          const catalogProductCollection: ICatalogProductFa[] = [
            {
              ...catalogProduct,
            },
            { id: 456 },
          ];
          expectedResult = service.addCatalogProductFaToCollectionIfMissing(catalogProductCollection, catalogProduct);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CatalogProductFa to an array that doesn't contain it", () => {
          const catalogProduct: ICatalogProductFa = { id: 123 };
          const catalogProductCollection: ICatalogProductFa[] = [{ id: 456 }];
          expectedResult = service.addCatalogProductFaToCollectionIfMissing(catalogProductCollection, catalogProduct);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogProduct);
        });

        it('should add only unique CatalogProductFa to an array', () => {
          const catalogProductArray: ICatalogProductFa[] = [{ id: 123 }, { id: 456 }, { id: 22579 }];
          const catalogProductCollection: ICatalogProductFa[] = [{ id: 123 }];
          expectedResult = service.addCatalogProductFaToCollectionIfMissing(catalogProductCollection, ...catalogProductArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const catalogProduct: ICatalogProductFa = { id: 123 };
          const catalogProduct2: ICatalogProductFa = { id: 456 };
          expectedResult = service.addCatalogProductFaToCollectionIfMissing([], catalogProduct, catalogProduct2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogProduct);
          expect(expectedResult).toContain(catalogProduct2);
        });

        it('should accept null and undefined values', () => {
          const catalogProduct: ICatalogProductFa = { id: 123 };
          expectedResult = service.addCatalogProductFaToCollectionIfMissing([], null, catalogProduct, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogProduct);
        });

        it('should return initial array if no CatalogProductFa is added', () => {
          const catalogProductCollection: ICatalogProductFa[] = [{ id: 123 }];
          expectedResult = service.addCatalogProductFaToCollectionIfMissing(catalogProductCollection, undefined, null);
          expect(expectedResult).toEqual(catalogProductCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
