import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICatalogService, CatalogService } from '../catalog-service.model';

import { CatalogServiceService } from './catalog-service.service';

describe('Service Tests', () => {
  describe('CatalogService Service', () => {
    let service: CatalogServiceService;
    let httpMock: HttpTestingController;
    let elemDefault: ICatalogService;
    let expectedResult: ICatalogService | ICatalogService[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CatalogServiceService);
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

      it('should create a CatalogService', () => {
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

        service.create(new CatalogService()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CatalogService', () => {
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

      it('should partial update a CatalogService', () => {
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
          new CatalogService()
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

      it('should return a list of CatalogService', () => {
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

      it('should delete a CatalogService', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCatalogServiceToCollectionIfMissing', () => {
        it('should add a CatalogService to an empty array', () => {
          const catalogService: ICatalogService = { id: 123 };
          expectedResult = service.addCatalogServiceToCollectionIfMissing([], catalogService);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogService);
        });

        it('should not add a CatalogService to an array that contains it', () => {
          const catalogService: ICatalogService = { id: 123 };
          const catalogServiceCollection: ICatalogService[] = [
            {
              ...catalogService,
            },
            { id: 456 },
          ];
          expectedResult = service.addCatalogServiceToCollectionIfMissing(catalogServiceCollection, catalogService);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CatalogService to an array that doesn't contain it", () => {
          const catalogService: ICatalogService = { id: 123 };
          const catalogServiceCollection: ICatalogService[] = [{ id: 456 }];
          expectedResult = service.addCatalogServiceToCollectionIfMissing(catalogServiceCollection, catalogService);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogService);
        });

        it('should add only unique CatalogService to an array', () => {
          const catalogServiceArray: ICatalogService[] = [{ id: 123 }, { id: 456 }, { id: 80252 }];
          const catalogServiceCollection: ICatalogService[] = [{ id: 123 }];
          expectedResult = service.addCatalogServiceToCollectionIfMissing(catalogServiceCollection, ...catalogServiceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const catalogService: ICatalogService = { id: 123 };
          const catalogService2: ICatalogService = { id: 456 };
          expectedResult = service.addCatalogServiceToCollectionIfMissing([], catalogService, catalogService2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalogService);
          expect(expectedResult).toContain(catalogService2);
        });

        it('should accept null and undefined values', () => {
          const catalogService: ICatalogService = { id: 123 };
          expectedResult = service.addCatalogServiceToCollectionIfMissing([], null, catalogService, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalogService);
        });

        it('should return initial array if no CatalogService is added', () => {
          const catalogServiceCollection: ICatalogService[] = [{ id: 123 }];
          expectedResult = service.addCatalogServiceToCollectionIfMissing(catalogServiceCollection, undefined, null);
          expect(expectedResult).toEqual(catalogServiceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
