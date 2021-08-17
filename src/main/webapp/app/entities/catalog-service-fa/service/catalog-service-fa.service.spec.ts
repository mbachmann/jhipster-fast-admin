import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICatalogServiceFa, CatalogServiceFa } from '../catalog-service-fa.model';

import { CatalogServiceFaService } from './catalog-service-fa.service';

describe('Service Tests', () => {
  describe('CatalogServiceFa Service', () => {
    let service: CatalogServiceFaService;
    let httpMock: HttpTestingController;
    let elemDefault: ICatalogServiceFa;
    let expectedResult: ICatalogServiceFa | ICatalogServiceFa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CatalogServiceFaService);
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

      it('should create a CatalogServiceFa', () => {
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

        service.create(new CatalogServiceFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CatalogServiceFa', () => {
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

      it('should partial update a CatalogServiceFa', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
            categoryName: 'BBBBBB',
            includingVat: true,
            unitName: 'BBBBBB',
            price: 1,
            defaultAmount: 1,
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          new CatalogServiceFa()
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

      it('should return a list of CatalogServiceFa', () => {
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

      it('should delete a CatalogServiceFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCatalogServiceFaToCollectionIfMissing', () => {
        it('should add a CatalogServiceFa to an empty array', () => {
          const catalogService: ICatalogServiceFa = { id: 123 };
          expectedResult = service.addCatalogServiceFaToCollectionIfMissing([], catalogService);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogService);
        });

        it('should not add a CatalogServiceFa to an array that contains it', () => {
          const catalogService: ICatalogServiceFa = { id: 123 };
          const catalogServiceCollection: ICatalogServiceFa[] = [
            {
              ...catalogService,
            },
            { id: 456 },
          ];
          expectedResult = service.addCatalogServiceFaToCollectionIfMissing(catalogServiceCollection, catalogService);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CatalogServiceFa to an array that doesn't contain it", () => {
          const catalogService: ICatalogServiceFa = { id: 123 };
          const catalogServiceCollection: ICatalogServiceFa[] = [{ id: 456 }];
          expectedResult = service.addCatalogServiceFaToCollectionIfMissing(catalogServiceCollection, catalogService);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogService);
        });

        it('should add only unique CatalogServiceFa to an array', () => {
          const catalogServiceArray: ICatalogServiceFa[] = [{ id: 123 }, { id: 456 }, { id: 80252 }];
          const catalogServiceCollection: ICatalogServiceFa[] = [{ id: 123 }];
          expectedResult = service.addCatalogServiceFaToCollectionIfMissing(catalogServiceCollection, ...catalogServiceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const catalogService: ICatalogServiceFa = { id: 123 };
          const catalogService2: ICatalogServiceFa = { id: 456 };
          expectedResult = service.addCatalogServiceFaToCollectionIfMissing([], catalogService, catalogService2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogService);
          expect(expectedResult).toContain(catalogService2);
        });

        it('should accept null and undefined values', () => {
          const catalogService: ICatalogServiceFa = { id: 123 };
          expectedResult = service.addCatalogServiceFaToCollectionIfMissing([], null, catalogService, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogService);
        });

        it('should return initial array if no CatalogServiceFa is added', () => {
          const catalogServiceCollection: ICatalogServiceFa[] = [{ id: 123 }];
          expectedResult = service.addCatalogServiceFaToCollectionIfMissing(catalogServiceCollection, undefined, null);
          expect(expectedResult).toEqual(catalogServiceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
