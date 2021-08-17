import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICustomFieldValue, CustomFieldValue } from '../custom-field-value.model';

import { CustomFieldValueService } from './custom-field-value.service';

describe('Service Tests', () => {
  describe('CustomFieldValue Service', () => {
    let service: CustomFieldValueService;
    let httpMock: HttpTestingController;
    let elemDefault: ICustomFieldValue;
    let expectedResult: ICustomFieldValue | ICustomFieldValue[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CustomFieldValueService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        key: 'AAAAAAA',
        name: 'AAAAAAA',
        value: 'AAAAAAA',
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

      it('should create a CustomFieldValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CustomFieldValue()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CustomFieldValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            value: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CustomFieldValue', () => {
        const patchObject = Object.assign({}, new CustomFieldValue());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CustomFieldValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            value: 'BBBBBB',
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

      it('should delete a CustomFieldValue', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCustomFieldValueToCollectionIfMissing', () => {
        it('should add a CustomFieldValue to an empty array', () => {
          const customFieldValue: ICustomFieldValue = { id: 123 };
          expectedResult = service.addCustomFieldValueToCollectionIfMissing([], customFieldValue);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customFieldValue);
        });

        it('should not add a CustomFieldValue to an array that contains it', () => {
          const customFieldValue: ICustomFieldValue = { id: 123 };
          const customFieldValueCollection: ICustomFieldValue[] = [
            {
              ...customFieldValue,
            },
            { id: 456 },
          ];
          expectedResult = service.addCustomFieldValueToCollectionIfMissing(customFieldValueCollection, customFieldValue);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CustomFieldValue to an array that doesn't contain it", () => {
          const customFieldValue: ICustomFieldValue = { id: 123 };
          const customFieldValueCollection: ICustomFieldValue[] = [{ id: 456 }];
          expectedResult = service.addCustomFieldValueToCollectionIfMissing(customFieldValueCollection, customFieldValue);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customFieldValue);
        });

        it('should add only unique CustomFieldValue to an array', () => {
          const customFieldValueArray: ICustomFieldValue[] = [{ id: 123 }, { id: 456 }, { id: 7226 }];
          const customFieldValueCollection: ICustomFieldValue[] = [{ id: 123 }];
          expectedResult = service.addCustomFieldValueToCollectionIfMissing(customFieldValueCollection, ...customFieldValueArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const customFieldValue: ICustomFieldValue = { id: 123 };
          const customFieldValue2: ICustomFieldValue = { id: 456 };
          expectedResult = service.addCustomFieldValueToCollectionIfMissing([], customFieldValue, customFieldValue2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customFieldValue);
          expect(expectedResult).toContain(customFieldValue2);
        });

        it('should accept null and undefined values', () => {
          const customFieldValue: ICustomFieldValue = { id: 123 };
          expectedResult = service.addCustomFieldValueToCollectionIfMissing([], null, customFieldValue, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customFieldValue);
        });

        it('should return initial array if no CustomFieldValue is added', () => {
          const customFieldValueCollection: ICustomFieldValue[] = [{ id: 123 }];
          expectedResult = service.addCustomFieldValueToCollectionIfMissing(customFieldValueCollection, undefined, null);
          expect(expectedResult).toEqual(customFieldValueCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
