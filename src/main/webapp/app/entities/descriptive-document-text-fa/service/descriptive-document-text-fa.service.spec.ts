import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DocumentInvoiceTextStatus } from 'app/entities/enumerations/document-invoice-text-status.model';
import { IDescriptiveDocumentTextFa, DescriptiveDocumentTextFa } from '../descriptive-document-text-fa.model';

import { DescriptiveDocumentTextFaService } from './descriptive-document-text-fa.service';

describe('Service Tests', () => {
  describe('DescriptiveDocumentTextFa Service', () => {
    let service: DescriptiveDocumentTextFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IDescriptiveDocumentTextFa;
    let expectedResult: IDescriptiveDocumentTextFa | IDescriptiveDocumentTextFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DescriptiveDocumentTextFaService);
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

      it('should create a DescriptiveDocumentTextFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DescriptiveDocumentTextFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DescriptiveDocumentTextFa', () => {
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

      it('should partial update a DescriptiveDocumentTextFa', () => {
        const patchObject = Object.assign(
          {
            introduction: 'BBBBBB',
            conditions: 'BBBBBB',
            status: 'BBBBBB',
          },
          new DescriptiveDocumentTextFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DescriptiveDocumentTextFa', () => {
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

      it('should delete a DescriptiveDocumentTextFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDescriptiveDocumentTextFaToCollectionIfMissing', () => {
        it('should add a DescriptiveDocumentTextFa to an empty array', () => {
          const descriptiveDocumentText: IDescriptiveDocumentTextFa = { id: 123 };
          expectedResult = service.addDescriptiveDocumentTextFaToCollectionIfMissing([], descriptiveDocumentText);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(descriptiveDocumentText);
        });

        it('should not add a DescriptiveDocumentTextFa to an array that contains it', () => {
          const descriptiveDocumentText: IDescriptiveDocumentTextFa = { id: 123 };
          const descriptiveDocumentTextCollection: IDescriptiveDocumentTextFa[] = [
            {
              ...descriptiveDocumentText,
            },
            { id: 456 },
          ];
          expectedResult = service.addDescriptiveDocumentTextFaToCollectionIfMissing(
            descriptiveDocumentTextCollection,
            descriptiveDocumentText
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DescriptiveDocumentTextFa to an array that doesn't contain it", () => {
          const descriptiveDocumentText: IDescriptiveDocumentTextFa = { id: 123 };
          const descriptiveDocumentTextCollection: IDescriptiveDocumentTextFa[] = [{ id: 456 }];
          expectedResult = service.addDescriptiveDocumentTextFaToCollectionIfMissing(
            descriptiveDocumentTextCollection,
            descriptiveDocumentText
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(descriptiveDocumentText);
        });

        it('should add only unique DescriptiveDocumentTextFa to an array', () => {
          const descriptiveDocumentTextArray: IDescriptiveDocumentTextFa[] = [{ id: 123 }, { id: 456 }, { id: 45008 }];
          const descriptiveDocumentTextCollection: IDescriptiveDocumentTextFa[] = [{ id: 123 }];
          expectedResult = service.addDescriptiveDocumentTextFaToCollectionIfMissing(
            descriptiveDocumentTextCollection,
            ...descriptiveDocumentTextArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const descriptiveDocumentText: IDescriptiveDocumentTextFa = { id: 123 };
          const descriptiveDocumentText2: IDescriptiveDocumentTextFa = { id: 456 };
          expectedResult = service.addDescriptiveDocumentTextFaToCollectionIfMissing([], descriptiveDocumentText, descriptiveDocumentText2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(descriptiveDocumentText);
          expect(expectedResult).toContain(descriptiveDocumentText2);
        });

        it('should accept null and undefined values', () => {
          const descriptiveDocumentText: IDescriptiveDocumentTextFa = { id: 123 };
          expectedResult = service.addDescriptiveDocumentTextFaToCollectionIfMissing([], null, descriptiveDocumentText, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(descriptiveDocumentText);
        });

        it('should return initial array if no DescriptiveDocumentTextFa is added', () => {
          const descriptiveDocumentTextCollection: IDescriptiveDocumentTextFa[] = [{ id: 123 }];
          expectedResult = service.addDescriptiveDocumentTextFaToCollectionIfMissing(descriptiveDocumentTextCollection, undefined, null);
          expect(expectedResult).toEqual(descriptiveDocumentTextCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
