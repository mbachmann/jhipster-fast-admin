import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { LetterStatus } from 'app/entities/enumerations/letter-status.model';
import { IDocumentLetterFa, DocumentLetterFa } from '../document-letter-fa.model';

import { DocumentLetterFaService } from './document-letter-fa.service';

describe('Service Tests', () => {
  describe('DocumentLetterFa Service', () => {
    let service: DocumentLetterFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumentLetterFa;
    let expectedResult: IDocumentLetterFa | IDocumentLetterFa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentLetterFaService);
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

      it('should create a DocumentLetterFa', () => {
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

        service.create(new DocumentLetterFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocumentLetterFa', () => {
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

      it('should partial update a DocumentLetterFa', () => {
        const patchObject = Object.assign(
          {
            contactName: 'BBBBBB',
            content: 'BBBBBB',
          },
          new DocumentLetterFa()
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

      it('should return a list of DocumentLetterFa', () => {
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

      it('should delete a DocumentLetterFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentLetterFaToCollectionIfMissing', () => {
        it('should add a DocumentLetterFa to an empty array', () => {
          const documentLetter: IDocumentLetterFa = { id: 123 };
          expectedResult = service.addDocumentLetterFaToCollectionIfMissing([], documentLetter);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentLetter);
        });

        it('should not add a DocumentLetterFa to an array that contains it', () => {
          const documentLetter: IDocumentLetterFa = { id: 123 };
          const documentLetterCollection: IDocumentLetterFa[] = [
            {
              ...documentLetter,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentLetterFaToCollectionIfMissing(documentLetterCollection, documentLetter);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocumentLetterFa to an array that doesn't contain it", () => {
          const documentLetter: IDocumentLetterFa = { id: 123 };
          const documentLetterCollection: IDocumentLetterFa[] = [{ id: 456 }];
          expectedResult = service.addDocumentLetterFaToCollectionIfMissing(documentLetterCollection, documentLetter);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentLetter);
        });

        it('should add only unique DocumentLetterFa to an array', () => {
          const documentLetterArray: IDocumentLetterFa[] = [{ id: 123 }, { id: 456 }, { id: 63069 }];
          const documentLetterCollection: IDocumentLetterFa[] = [{ id: 123 }];
          expectedResult = service.addDocumentLetterFaToCollectionIfMissing(documentLetterCollection, ...documentLetterArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documentLetter: IDocumentLetterFa = { id: 123 };
          const documentLetter2: IDocumentLetterFa = { id: 456 };
          expectedResult = service.addDocumentLetterFaToCollectionIfMissing([], documentLetter, documentLetter2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentLetter);
          expect(expectedResult).toContain(documentLetter2);
        });

        it('should accept null and undefined values', () => {
          const documentLetter: IDocumentLetterFa = { id: 123 };
          expectedResult = service.addDocumentLetterFaToCollectionIfMissing([], null, documentLetter, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentLetter);
        });

        it('should return initial array if no DocumentLetterFa is added', () => {
          const documentLetterCollection: IDocumentLetterFa[] = [{ id: 123 }];
          expectedResult = service.addDocumentLetterFaToCollectionIfMissing(documentLetterCollection, undefined, null);
          expect(expectedResult).toEqual(documentLetterCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
