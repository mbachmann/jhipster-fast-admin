import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { IFreeText, FreeText } from '../free-text.model';

import { FreeTextService } from './free-text.service';

describe('Service Tests', () => {
  describe('FreeText Service', () => {
    let service: FreeTextService;
    let httpMock: HttpTestingController;
    let elemDefault: IFreeText;
    let expectedResult: IFreeText | IFreeText[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FreeTextService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        text: 'AAAAAAA',
        fontSize: 0,
        positionX: 0,
        positionY: 0,
        pageNo: 0,
        language: DocumentLanguage.FRENCH,
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

      it('should create a FreeText', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FreeText()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FreeText', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            text: 'BBBBBB',
            fontSize: 1,
            positionX: 1,
            positionY: 1,
            pageNo: 1,
            language: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FreeText', () => {
        const patchObject = Object.assign(
          {
            text: 'BBBBBB',
            language: 'BBBBBB',
          },
          new FreeText()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FreeText', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            text: 'BBBBBB',
            fontSize: 1,
            positionX: 1,
            positionY: 1,
            pageNo: 1,
            language: 'BBBBBB',
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

      it('should delete a FreeText', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFreeTextToCollectionIfMissing', () => {
        it('should add a FreeText to an empty array', () => {
          const freeText: IFreeText = { id: 123 };
          expectedResult = service.addFreeTextToCollectionIfMissing([], freeText);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(freeText);
        });

        it('should not add a FreeText to an array that contains it', () => {
          const freeText: IFreeText = { id: 123 };
          const freeTextCollection: IFreeText[] = [
            {
              ...freeText,
            },
            { id: 456 },
          ];
          expectedResult = service.addFreeTextToCollectionIfMissing(freeTextCollection, freeText);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FreeText to an array that doesn't contain it", () => {
          const freeText: IFreeText = { id: 123 };
          const freeTextCollection: IFreeText[] = [{ id: 456 }];
          expectedResult = service.addFreeTextToCollectionIfMissing(freeTextCollection, freeText);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(freeText);
        });

        it('should add only unique FreeText to an array', () => {
          const freeTextArray: IFreeText[] = [{ id: 123 }, { id: 456 }, { id: 63633 }];
          const freeTextCollection: IFreeText[] = [{ id: 123 }];
          expectedResult = service.addFreeTextToCollectionIfMissing(freeTextCollection, ...freeTextArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const freeText: IFreeText = { id: 123 };
          const freeText2: IFreeText = { id: 456 };
          expectedResult = service.addFreeTextToCollectionIfMissing([], freeText, freeText2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(freeText);
          expect(expectedResult).toContain(freeText2);
        });

        it('should accept null and undefined values', () => {
          const freeText: IFreeText = { id: 123 };
          expectedResult = service.addFreeTextToCollectionIfMissing([], null, freeText, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(freeText);
        });

        it('should return initial array if no FreeText is added', () => {
          const freeTextCollection: IFreeText[] = [{ id: 123 }];
          expectedResult = service.addFreeTextToCollectionIfMissing(freeTextCollection, undefined, null);
          expect(expectedResult).toEqual(freeTextCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
