import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DocumentInvoiceTextStatus } from 'app/entities/enumerations/document-invoice-text-status.model';
import { IDescriptiveDocumentText, DescriptiveDocumentText } from '../descriptive-document-text.model';

import { DescriptiveDocumentTextService } from './descriptive-document-text.service';

describe('Service Tests', () => {
  describe('DescriptiveDocumentText Service', () => {
    let service: DescriptiveDocumentTextService;
    let httpMock: HttpTestingController;
    let elemDefault: IDescriptiveDocumentText;
    let expectedResult: IDescriptiveDocumentText | IDescriptiveDocumentText[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DescriptiveDocumentTextService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        introduction: 'AAAAAAA',
        conditions: 'AAAAAAA',
        status: DocumentInvoiceTextStatus.DEFAULT,
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

      it('should create a DescriptiveDocumentText', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DescriptiveDocumentText()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DescriptiveDocumentText', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            introduction: 'BBBBBB',
            conditions: 'BBBBBB',
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DescriptiveDocumentText', () => {
        const patchObject = Object.assign(
          {
            introduction: 'BBBBBB',
            conditions: 'BBBBBB',
            status: 'BBBBBB',
          },
          new DescriptiveDocumentText()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DescriptiveDocumentText', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            introduction: 'BBBBBB',
            conditions: 'BBBBBB',
            status: 'BBBBBB',
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

      it('should delete a DescriptiveDocumentText', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDescriptiveDocumentTextToCollectionIfMissing', () => {
        it('should add a DescriptiveDocumentText to an empty array', () => {
          const descriptiveDocumentText: IDescriptiveDocumentText = { id: 123 };
          expectedResult = service.addDescriptiveDocumentTextToCollectionIfMissing([], descriptiveDocumentText);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(descriptiveDocumentText);
        });

        it('should not add a DescriptiveDocumentText to an array that contains it', () => {
          const descriptiveDocumentText: IDescriptiveDocumentText = { id: 123 };
          const descriptiveDocumentTextCollection: IDescriptiveDocumentText[] = [
            {
              ...descriptiveDocumentText,
            },
            { id: 456 },
          ];
          expectedResult = service.addDescriptiveDocumentTextToCollectionIfMissing(
            descriptiveDocumentTextCollection,
            descriptiveDocumentText
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DescriptiveDocumentText to an array that doesn't contain it", () => {
          const descriptiveDocumentText: IDescriptiveDocumentText = { id: 123 };
          const descriptiveDocumentTextCollection: IDescriptiveDocumentText[] = [{ id: 456 }];
          expectedResult = service.addDescriptiveDocumentTextToCollectionIfMissing(
            descriptiveDocumentTextCollection,
            descriptiveDocumentText
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(descriptiveDocumentText);
        });

        it('should add only unique DescriptiveDocumentText to an array', () => {
          const descriptiveDocumentTextArray: IDescriptiveDocumentText[] = [{ id: 123 }, { id: 456 }, { id: 45008 }];
          const descriptiveDocumentTextCollection: IDescriptiveDocumentText[] = [{ id: 123 }];
          expectedResult = service.addDescriptiveDocumentTextToCollectionIfMissing(
            descriptiveDocumentTextCollection,
            ...descriptiveDocumentTextArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const descriptiveDocumentText: IDescriptiveDocumentText = { id: 123 };
          const descriptiveDocumentText2: IDescriptiveDocumentText = { id: 456 };
          expectedResult = service.addDescriptiveDocumentTextToCollectionIfMissing([], descriptiveDocumentText, descriptiveDocumentText2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(descriptiveDocumentText);
          expect(expectedResult).toContain(descriptiveDocumentText2);
        });

        it('should accept null and undefined values', () => {
          const descriptiveDocumentText: IDescriptiveDocumentText = { id: 123 };
          expectedResult = service.addDescriptiveDocumentTextToCollectionIfMissing([], null, descriptiveDocumentText, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(descriptiveDocumentText);
        });

        it('should return initial array if no DescriptiveDocumentText is added', () => {
          const descriptiveDocumentTextCollection: IDescriptiveDocumentText[] = [{ id: 123 }];
          expectedResult = service.addDescriptiveDocumentTextToCollectionIfMissing(descriptiveDocumentTextCollection, undefined, null);
          expect(expectedResult).toEqual(descriptiveDocumentTextCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
