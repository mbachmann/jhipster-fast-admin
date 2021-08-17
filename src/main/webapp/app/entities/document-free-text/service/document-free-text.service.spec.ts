import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDocumentFreeText, DocumentFreeText } from '../document-free-text.model';

import { DocumentFreeTextService } from './document-free-text.service';

describe('Service Tests', () => {
  describe('DocumentFreeText Service', () => {
    let service: DocumentFreeTextService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumentFreeText;
    let expectedResult: IDocumentFreeText | IDocumentFreeText[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentFreeTextService);
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

      it('should create a DocumentFreeText', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DocumentFreeText()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocumentFreeText', () => {
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

      it('should partial update a DocumentFreeText', () => {
        const patchObject = Object.assign(
          {
            text: 'BBBBBB',
            positionX: 1,
            positionY: 1,
          },
          new DocumentFreeText()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DocumentFreeText', () => {
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

      it('should delete a DocumentFreeText', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentFreeTextToCollectionIfMissing', () => {
        it('should add a DocumentFreeText to an empty array', () => {
          const documentFreeText: IDocumentFreeText = { id: 123 };
          expectedResult = service.addDocumentFreeTextToCollectionIfMissing([], documentFreeText);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentFreeText);
        });

        it('should not add a DocumentFreeText to an array that contains it', () => {
          const documentFreeText: IDocumentFreeText = { id: 123 };
          const documentFreeTextCollection: IDocumentFreeText[] = [
            {
              ...documentFreeText,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentFreeTextToCollectionIfMissing(documentFreeTextCollection, documentFreeText);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocumentFreeText to an array that doesn't contain it", () => {
          const documentFreeText: IDocumentFreeText = { id: 123 };
          const documentFreeTextCollection: IDocumentFreeText[] = [{ id: 456 }];
          expectedResult = service.addDocumentFreeTextToCollectionIfMissing(documentFreeTextCollection, documentFreeText);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentFreeText);
        });

        it('should add only unique DocumentFreeText to an array', () => {
          const documentFreeTextArray: IDocumentFreeText[] = [{ id: 123 }, { id: 456 }, { id: 52482 }];
          const documentFreeTextCollection: IDocumentFreeText[] = [{ id: 123 }];
          expectedResult = service.addDocumentFreeTextToCollectionIfMissing(documentFreeTextCollection, ...documentFreeTextArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documentFreeText: IDocumentFreeText = { id: 123 };
          const documentFreeText2: IDocumentFreeText = { id: 456 };
          expectedResult = service.addDocumentFreeTextToCollectionIfMissing([], documentFreeText, documentFreeText2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentFreeText);
          expect(expectedResult).toContain(documentFreeText2);
        });

        it('should accept null and undefined values', () => {
          const documentFreeText: IDocumentFreeText = { id: 123 };
          expectedResult = service.addDocumentFreeTextToCollectionIfMissing([], null, documentFreeText, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentFreeText);
        });

        it('should return initial array if no DocumentFreeText is added', () => {
          const documentFreeTextCollection: IDocumentFreeText[] = [{ id: 123 }];
          expectedResult = service.addDocumentFreeTextToCollectionIfMissing(documentFreeTextCollection, undefined, null);
          expect(expectedResult).toEqual(documentFreeTextCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
