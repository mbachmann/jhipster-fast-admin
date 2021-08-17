import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { DocumentTextUsage } from 'app/entities/enumerations/document-text-usage.model';
import { DocumentInvoiceTextStatus } from 'app/entities/enumerations/document-invoice-text-status.model';
import { DocumentTextType } from 'app/entities/enumerations/document-text-type.model';
import { DocumentTextDocumentType } from 'app/entities/enumerations/document-text-document-type.model';
import { IDocumentText, DocumentText } from '../document-text.model';

import { DocumentTextService } from './document-text.service';

describe('Service Tests', () => {
  describe('DocumentText Service', () => {
    let service: DocumentTextService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumentText;
    let expectedResult: IDocumentText | IDocumentText[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentTextService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        defaultText: false,
        text: 'AAAAAAA',
        language: DocumentLanguage.FRENCH,
        usage: DocumentTextUsage.TITLE,
        status: DocumentInvoiceTextStatus.DEFAULT,
        type: DocumentTextType.DOCUMENT,
        documentType: DocumentTextDocumentType.OFFER,
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

      it('should create a DocumentText', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DocumentText()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocumentText', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            defaultText: true,
            text: 'BBBBBB',
            language: 'BBBBBB',
            usage: 'BBBBBB',
            status: 'BBBBBB',
            type: 'BBBBBB',
            documentType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DocumentText', () => {
        const patchObject = Object.assign(
          {
            defaultText: true,
            text: 'BBBBBB',
            status: 'BBBBBB',
            type: 'BBBBBB',
            documentType: 'BBBBBB',
          },
          new DocumentText()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DocumentText', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            defaultText: true,
            text: 'BBBBBB',
            language: 'BBBBBB',
            usage: 'BBBBBB',
            status: 'BBBBBB',
            type: 'BBBBBB',
            documentType: 'BBBBBB',
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

      it('should delete a DocumentText', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentTextToCollectionIfMissing', () => {
        it('should add a DocumentText to an empty array', () => {
          const documentText: IDocumentText = { id: 123 };
          expectedResult = service.addDocumentTextToCollectionIfMissing([], documentText);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentText);
        });

        it('should not add a DocumentText to an array that contains it', () => {
          const documentText: IDocumentText = { id: 123 };
          const documentTextCollection: IDocumentText[] = [
            {
              ...documentText,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentTextToCollectionIfMissing(documentTextCollection, documentText);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocumentText to an array that doesn't contain it", () => {
          const documentText: IDocumentText = { id: 123 };
          const documentTextCollection: IDocumentText[] = [{ id: 456 }];
          expectedResult = service.addDocumentTextToCollectionIfMissing(documentTextCollection, documentText);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentText);
        });

        it('should add only unique DocumentText to an array', () => {
          const documentTextArray: IDocumentText[] = [{ id: 123 }, { id: 456 }, { id: 8259 }];
          const documentTextCollection: IDocumentText[] = [{ id: 123 }];
          expectedResult = service.addDocumentTextToCollectionIfMissing(documentTextCollection, ...documentTextArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documentText: IDocumentText = { id: 123 };
          const documentText2: IDocumentText = { id: 456 };
          expectedResult = service.addDocumentTextToCollectionIfMissing([], documentText, documentText2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentText);
          expect(expectedResult).toContain(documentText2);
        });

        it('should accept null and undefined values', () => {
          const documentText: IDocumentText = { id: 123 };
          expectedResult = service.addDocumentTextToCollectionIfMissing([], null, documentText, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentText);
        });

        it('should return initial array if no DocumentText is added', () => {
          const documentTextCollection: IDocumentText[] = [{ id: 123 }];
          expectedResult = service.addDocumentTextToCollectionIfMissing(documentTextCollection, undefined, null);
          expect(expectedResult).toEqual(documentTextCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
