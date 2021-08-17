import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { LetterStatus } from 'app/entities/enumerations/letter-status.model';
import { IDocumentLetter, DocumentLetter } from '../document-letter.model';

import { DocumentLetterService } from './document-letter.service';

describe('Service Tests', () => {
  describe('DocumentLetter Service', () => {
    let service: DocumentLetterService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumentLetter;
    let expectedResult: IDocumentLetter | IDocumentLetter[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentLetterService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        contactName: 'AAAAAAA',
        date: currentDate,
        title: 'AAAAAAA',
        content: 'AAAAAAA',
        language: DocumentLanguage.FRENCH,
        pageAmount: 0,
        notes: 'AAAAAAA',
        status: LetterStatus.DRAFT,
        created: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a DocumentLetter', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.create(new DocumentLetter()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocumentLetter', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            contactName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            title: 'BBBBBB',
            content: 'BBBBBB',
            language: 'BBBBBB',
            pageAmount: 1,
            notes: 'BBBBBB',
            status: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DocumentLetter', () => {
        const patchObject = Object.assign(
          {
            contactName: 'BBBBBB',
            content: 'BBBBBB',
          },
          new DocumentLetter()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DocumentLetter', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            contactName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            title: 'BBBBBB',
            content: 'BBBBBB',
            language: 'BBBBBB',
            pageAmount: 1,
            notes: 'BBBBBB',
            status: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
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

      it('should delete a DocumentLetter', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentLetterToCollectionIfMissing', () => {
        it('should add a DocumentLetter to an empty array', () => {
          const documentLetter: IDocumentLetter = { id: 123 };
          expectedResult = service.addDocumentLetterToCollectionIfMissing([], documentLetter);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentLetter);
        });

        it('should not add a DocumentLetter to an array that contains it', () => {
          const documentLetter: IDocumentLetter = { id: 123 };
          const documentLetterCollection: IDocumentLetter[] = [
            {
              ...documentLetter,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentLetterToCollectionIfMissing(documentLetterCollection, documentLetter);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocumentLetter to an array that doesn't contain it", () => {
          const documentLetter: IDocumentLetter = { id: 123 };
          const documentLetterCollection: IDocumentLetter[] = [{ id: 456 }];
          expectedResult = service.addDocumentLetterToCollectionIfMissing(documentLetterCollection, documentLetter);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentLetter);
        });

        it('should add only unique DocumentLetter to an array', () => {
          const documentLetterArray: IDocumentLetter[] = [{ id: 123 }, { id: 456 }, { id: 63069 }];
          const documentLetterCollection: IDocumentLetter[] = [{ id: 123 }];
          expectedResult = service.addDocumentLetterToCollectionIfMissing(documentLetterCollection, ...documentLetterArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documentLetter: IDocumentLetter = { id: 123 };
          const documentLetter2: IDocumentLetter = { id: 456 };
          expectedResult = service.addDocumentLetterToCollectionIfMissing([], documentLetter, documentLetter2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentLetter);
          expect(expectedResult).toContain(documentLetter2);
        });

        it('should accept null and undefined values', () => {
          const documentLetter: IDocumentLetter = { id: 123 };
          expectedResult = service.addDocumentLetterToCollectionIfMissing([], null, documentLetter, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentLetter);
        });

        it('should return initial array if no DocumentLetter is added', () => {
          const documentLetterCollection: IDocumentLetter[] = [{ id: 123 }];
          expectedResult = service.addDocumentLetterToCollectionIfMissing(documentLetterCollection, undefined, null);
          expect(expectedResult).toEqual(documentLetterCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
