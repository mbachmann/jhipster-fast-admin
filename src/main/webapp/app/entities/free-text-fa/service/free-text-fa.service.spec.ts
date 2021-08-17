import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DocumentLanguage } from 'app/entities/enumerations/document-language.model';
import { IFreeTextFa, FreeTextFa } from '../free-text-fa.model';

import { FreeTextFaService } from './free-text-fa.service';

describe('Service Tests', () => {
  describe('FreeTextFa Service', () => {
    let service: FreeTextFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IFreeTextFa;
    let expectedResult: IFreeTextFa | IFreeTextFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FreeTextFaService);
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

      it('should create a FreeTextFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FreeTextFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FreeTextFa', () => {
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

      it('should partial update a FreeTextFa', () => {
        const patchObject = Object.assign(
          {
            text: 'BBBBBB',
            language: 'BBBBBB',
          },
          new FreeTextFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FreeTextFa', () => {
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

      it('should delete a FreeTextFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFreeTextFaToCollectionIfMissing', () => {
        it('should add a FreeTextFa to an empty array', () => {
          const freeText: IFreeTextFa = { id: 123 };
          expectedResult = service.addFreeTextFaToCollectionIfMissing([], freeText);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(freeText);
        });

        it('should not add a FreeTextFa to an array that contains it', () => {
          const freeText: IFreeTextFa = { id: 123 };
          const freeTextCollection: IFreeTextFa[] = [
            {
              ...freeText,
            },
            { id: 456 },
          ];
          expectedResult = service.addFreeTextFaToCollectionIfMissing(freeTextCollection, freeText);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FreeTextFa to an array that doesn't contain it", () => {
          const freeText: IFreeTextFa = { id: 123 };
          const freeTextCollection: IFreeTextFa[] = [{ id: 456 }];
          expectedResult = service.addFreeTextFaToCollectionIfMissing(freeTextCollection, freeText);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(freeText);
        });

        it('should add only unique FreeTextFa to an array', () => {
          const freeTextArray: IFreeTextFa[] = [{ id: 123 }, { id: 456 }, { id: 63633 }];
          const freeTextCollection: IFreeTextFa[] = [{ id: 123 }];
          expectedResult = service.addFreeTextFaToCollectionIfMissing(freeTextCollection, ...freeTextArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const freeText: IFreeTextFa = { id: 123 };
          const freeText2: IFreeTextFa = { id: 456 };
          expectedResult = service.addFreeTextFaToCollectionIfMissing([], freeText, freeText2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(freeText);
          expect(expectedResult).toContain(freeText2);
        });

        it('should accept null and undefined values', () => {
          const freeText: IFreeTextFa = { id: 123 };
          expectedResult = service.addFreeTextFaToCollectionIfMissing([], null, freeText, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(freeText);
        });

        it('should return initial array if no FreeTextFa is added', () => {
          const freeTextCollection: IFreeTextFa[] = [{ id: 123 }];
          expectedResult = service.addFreeTextFaToCollectionIfMissing(freeTextCollection, undefined, null);
          expect(expectedResult).toEqual(freeTextCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
