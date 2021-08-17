import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { DocumentTextUsage } from 'app/entities/enumerations/document-text-usage.model';
import { DocumentInvoiceTextStatus } from 'app/entities/enumerations/document-invoice-text-status.model';
import { DocumentTextType } from 'app/entities/enumerations/document-text-type.model';
import { DocumentTextDocumentType } from 'app/entities/enumerations/document-text-document-type.model';
import { IDocumentTextFa, DocumentTextFa } from '../document-text-fa.model';

import { DocumentTextFaService } from './document-text-fa.service';

describe('Service Tests', () => {
  describe('DocumentTextFa Service', () => {
    let service: DocumentTextFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumentTextFa;
    let expectedResult: IDocumentTextFa | IDocumentTextFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentTextFaService);
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

      it('should create a DocumentTextFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DocumentTextFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocumentTextFa', () => {
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

      it('should partial update a DocumentTextFa', () => {
        const patchObject = Object.assign(
          {
            defaultText: true,
            text: 'BBBBBB',
            status: 'BBBBBB',
            type: 'BBBBBB',
            documentType: 'BBBBBB',
          },
          new DocumentTextFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DocumentTextFa', () => {
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

      it('should delete a DocumentTextFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentTextFaToCollectionIfMissing', () => {
        it('should add a DocumentTextFa to an empty array', () => {
          const documentText: IDocumentTextFa = { id: 123 };
          expectedResult = service.addDocumentTextFaToCollectionIfMissing([], documentText);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentText);
        });

        it('should not add a DocumentTextFa to an array that contains it', () => {
          const documentText: IDocumentTextFa = { id: 123 };
          const documentTextCollection: IDocumentTextFa[] = [
            {
              ...documentText,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentTextFaToCollectionIfMissing(documentTextCollection, documentText);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocumentTextFa to an array that doesn't contain it", () => {
          const documentText: IDocumentTextFa = { id: 123 };
          const documentTextCollection: IDocumentTextFa[] = [{ id: 456 }];
          expectedResult = service.addDocumentTextFaToCollectionIfMissing(documentTextCollection, documentText);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentText);
        });

        it('should add only unique DocumentTextFa to an array', () => {
          const documentTextArray: IDocumentTextFa[] = [{ id: 123 }, { id: 456 }, { id: 8259 }];
          const documentTextCollection: IDocumentTextFa[] = [{ id: 123 }];
          expectedResult = service.addDocumentTextFaToCollectionIfMissing(documentTextCollection, ...documentTextArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documentText: IDocumentTextFa = { id: 123 };
          const documentText2: IDocumentTextFa = { id: 456 };
          expectedResult = service.addDocumentTextFaToCollectionIfMissing([], documentText, documentText2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentText);
          expect(expectedResult).toContain(documentText2);
        });

        it('should accept null and undefined values', () => {
          const documentText: IDocumentTextFa = { id: 123 };
          expectedResult = service.addDocumentTextFaToCollectionIfMissing([], null, documentText, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentText);
        });

        it('should return initial array if no DocumentTextFa is added', () => {
          const documentTextCollection: IDocumentTextFa[] = [{ id: 123 }];
          expectedResult = service.addDocumentTextFaToCollectionIfMissing(documentTextCollection, undefined, null);
          expect(expectedResult).toEqual(documentTextCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
