import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDocumentFreeTextFa, DocumentFreeTextFa } from '../document-free-text-fa.model';

import { DocumentFreeTextFaService } from './document-free-text-fa.service';

describe('Service Tests', () => {
  describe('DocumentFreeTextFa Service', () => {
    let service: DocumentFreeTextFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumentFreeTextFa;
    let expectedResult: IDocumentFreeTextFa | IDocumentFreeTextFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentFreeTextFaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        text: 'AAAAAAA',
        fontSize: 0,
        positionX: 0,
        positionY: 0,
        pageNo: 0,
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

      it('should create a DocumentFreeTextFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DocumentFreeTextFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocumentFreeTextFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            text: 'BBBBBB',
            fontSize: 1,
            positionX: 1,
            positionY: 1,
            pageNo: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DocumentFreeTextFa', () => {
        const patchObject = Object.assign(
          {
            text: 'BBBBBB',
            positionX: 1,
            positionY: 1,
          },
          new DocumentFreeTextFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DocumentFreeTextFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            text: 'BBBBBB',
            fontSize: 1,
            positionX: 1,
            positionY: 1,
            pageNo: 1,
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

      it('should delete a DocumentFreeTextFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentFreeTextFaToCollectionIfMissing', () => {
        it('should add a DocumentFreeTextFa to an empty array', () => {
          const documentFreeText: IDocumentFreeTextFa = { id: 123 };
          expectedResult = service.addDocumentFreeTextFaToCollectionIfMissing([], documentFreeText);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentFreeText);
        });

        it('should not add a DocumentFreeTextFa to an array that contains it', () => {
          const documentFreeText: IDocumentFreeTextFa = { id: 123 };
          const documentFreeTextCollection: IDocumentFreeTextFa[] = [
            {
              ...documentFreeText,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentFreeTextFaToCollectionIfMissing(documentFreeTextCollection, documentFreeText);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocumentFreeTextFa to an array that doesn't contain it", () => {
          const documentFreeText: IDocumentFreeTextFa = { id: 123 };
          const documentFreeTextCollection: IDocumentFreeTextFa[] = [{ id: 456 }];
          expectedResult = service.addDocumentFreeTextFaToCollectionIfMissing(documentFreeTextCollection, documentFreeText);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentFreeText);
        });

        it('should add only unique DocumentFreeTextFa to an array', () => {
          const documentFreeTextArray: IDocumentFreeTextFa[] = [{ id: 123 }, { id: 456 }, { id: 52482 }];
          const documentFreeTextCollection: IDocumentFreeTextFa[] = [{ id: 123 }];
          expectedResult = service.addDocumentFreeTextFaToCollectionIfMissing(documentFreeTextCollection, ...documentFreeTextArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documentFreeText: IDocumentFreeTextFa = { id: 123 };
          const documentFreeText2: IDocumentFreeTextFa = { id: 456 };
          expectedResult = service.addDocumentFreeTextFaToCollectionIfMissing([], documentFreeText, documentFreeText2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentFreeText);
          expect(expectedResult).toContain(documentFreeText2);
        });

        it('should accept null and undefined values', () => {
          const documentFreeText: IDocumentFreeTextFa = { id: 123 };
          expectedResult = service.addDocumentFreeTextFaToCollectionIfMissing([], null, documentFreeText, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentFreeText);
        });

        it('should return initial array if no DocumentFreeTextFa is added', () => {
          const documentFreeTextCollection: IDocumentFreeTextFa[] = [{ id: 123 }];
          expectedResult = service.addDocumentFreeTextFaToCollectionIfMissing(documentFreeTextCollection, undefined, null);
          expect(expectedResult).toEqual(documentFreeTextCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
