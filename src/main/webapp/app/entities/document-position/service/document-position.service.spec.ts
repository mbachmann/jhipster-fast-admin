import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DocumentPositionType } from 'app/entities/enumerations/document-position-type.model';
import { CatalogScope } from 'app/entities/enumerations/catalog-scope.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';
import { IDocumentPosition, DocumentPosition } from '../document-position.model';

import { DocumentPositionService } from './document-position.service';

describe('Service Tests', () => {
  describe('DocumentPosition Service', () => {
    let service: DocumentPositionService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumentPosition;
    let expectedResult: IDocumentPosition | IDocumentPosition[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentPositionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        type: DocumentPositionType.NORMAL,
        catalogType: CatalogScope.SERVICE,
        number: 'AAAAAAA',
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        price: 0,
        vat: 0,
        amount: 0,
        discountRate: 0,
        discountType: DiscountType.IN_PERCENT,
        total: 0,
        showOnlyTotal: false,
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

      it('should create a DocumentPosition', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DocumentPosition()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocumentPosition', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            type: 'BBBBBB',
            catalogType: 'BBBBBB',
            number: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            price: 1,
            vat: 1,
            amount: 1,
            discountRate: 1,
            discountType: 'BBBBBB',
            total: 1,
            showOnlyTotal: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DocumentPosition', () => {
        const patchObject = Object.assign(
          {
            number: 'BBBBBB',
            price: 1,
            amount: 1,
            discountType: 'BBBBBB',
          },
          new DocumentPosition()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DocumentPosition', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            type: 'BBBBBB',
            catalogType: 'BBBBBB',
            number: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            price: 1,
            vat: 1,
            amount: 1,
            discountRate: 1,
            discountType: 'BBBBBB',
            total: 1,
            showOnlyTotal: true,
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

      it('should delete a DocumentPosition', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentPositionToCollectionIfMissing', () => {
        it('should add a DocumentPosition to an empty array', () => {
          const documentPosition: IDocumentPosition = { id: 123 };
          expectedResult = service.addDocumentPositionToCollectionIfMissing([], documentPosition);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentPosition);
        });

        it('should not add a DocumentPosition to an array that contains it', () => {
          const documentPosition: IDocumentPosition = { id: 123 };
          const documentPositionCollection: IDocumentPosition[] = [
            {
              ...documentPosition,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentPositionToCollectionIfMissing(documentPositionCollection, documentPosition);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocumentPosition to an array that doesn't contain it", () => {
          const documentPosition: IDocumentPosition = { id: 123 };
          const documentPositionCollection: IDocumentPosition[] = [{ id: 456 }];
          expectedResult = service.addDocumentPositionToCollectionIfMissing(documentPositionCollection, documentPosition);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentPosition);
        });

        it('should add only unique DocumentPosition to an array', () => {
          const documentPositionArray: IDocumentPosition[] = [{ id: 123 }, { id: 456 }, { id: 14850 }];
          const documentPositionCollection: IDocumentPosition[] = [{ id: 123 }];
          expectedResult = service.addDocumentPositionToCollectionIfMissing(documentPositionCollection, ...documentPositionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documentPosition: IDocumentPosition = { id: 123 };
          const documentPosition2: IDocumentPosition = { id: 456 };
          expectedResult = service.addDocumentPositionToCollectionIfMissing([], documentPosition, documentPosition2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentPosition);
          expect(expectedResult).toContain(documentPosition2);
        });

        it('should accept null and undefined values', () => {
          const documentPosition: IDocumentPosition = { id: 123 };
          expectedResult = service.addDocumentPositionToCollectionIfMissing([], null, documentPosition, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentPosition);
        });

        it('should return initial array if no DocumentPosition is added', () => {
          const documentPositionCollection: IDocumentPosition[] = [{ id: 123 }];
          expectedResult = service.addDocumentPositionToCollectionIfMissing(documentPositionCollection, undefined, null);
          expect(expectedResult).toEqual(documentPositionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
