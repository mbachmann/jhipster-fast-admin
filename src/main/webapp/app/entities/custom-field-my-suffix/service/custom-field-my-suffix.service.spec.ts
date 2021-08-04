import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DomainResource } from 'app/entities/enumerations/domain-resource.model';
import { ICustomFieldMySuffix, CustomFieldMySuffix } from '../custom-field-my-suffix.model';

import { CustomFieldMySuffixService } from './custom-field-my-suffix.service';

describe('Service Tests', () => {
  describe('CustomFieldMySuffix Service', () => {
    let service: CustomFieldMySuffixService;
    let httpMock: HttpTestingController;
    let elemDefault: ICustomFieldMySuffix;
    let expectedResult: ICustomFieldMySuffix | ICustomFieldMySuffix[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CustomFieldMySuffixService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        domainResource: DomainResource.AFFILIATE,
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

      it('should create a CustomFieldMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CustomFieldMySuffix()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CustomFieldMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            domainResource: 'BBBBBB',
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

      it('should partial update a CustomFieldMySuffix', () => {
        const patchObject = Object.assign(
          {
            key: 'BBBBBB',
            name: 'BBBBBB',
          },
          new CustomFieldMySuffix()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CustomFieldMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            domainResource: 'BBBBBB',
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

      it('should delete a CustomFieldMySuffix', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCustomFieldMySuffixToCollectionIfMissing', () => {
        it('should add a CustomFieldMySuffix to an empty array', () => {
          const customField: ICustomFieldMySuffix = { id: 123 };
          expectedResult = service.addCustomFieldMySuffixToCollectionIfMissing([], customField);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customField);
        });

        it('should not add a CustomFieldMySuffix to an array that contains it', () => {
          const customField: ICustomFieldMySuffix = { id: 123 };
          const customFieldCollection: ICustomFieldMySuffix[] = [
            {
              ...customField,
            },
            { id: 456 },
          ];
          expectedResult = service.addCustomFieldMySuffixToCollectionIfMissing(customFieldCollection, customField);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CustomFieldMySuffix to an array that doesn't contain it", () => {
          const customField: ICustomFieldMySuffix = { id: 123 };
          const customFieldCollection: ICustomFieldMySuffix[] = [{ id: 456 }];
          expectedResult = service.addCustomFieldMySuffixToCollectionIfMissing(customFieldCollection, customField);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customField);
        });

        it('should add only unique CustomFieldMySuffix to an array', () => {
          const customFieldArray: ICustomFieldMySuffix[] = [{ id: 123 }, { id: 456 }, { id: 94529 }];
          const customFieldCollection: ICustomFieldMySuffix[] = [{ id: 123 }];
          expectedResult = service.addCustomFieldMySuffixToCollectionIfMissing(customFieldCollection, ...customFieldArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const customField: ICustomFieldMySuffix = { id: 123 };
          const customField2: ICustomFieldMySuffix = { id: 456 };
          expectedResult = service.addCustomFieldMySuffixToCollectionIfMissing([], customField, customField2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customField);
          expect(expectedResult).toContain(customField2);
        });

        it('should accept null and undefined values', () => {
          const customField: ICustomFieldMySuffix = { id: 123 };
          expectedResult = service.addCustomFieldMySuffixToCollectionIfMissing([], null, customField, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customField);
        });

        it('should return initial array if no CustomFieldMySuffix is added', () => {
          const customFieldCollection: ICustomFieldMySuffix[] = [{ id: 123 }];
          expectedResult = service.addCustomFieldMySuffixToCollectionIfMissing(customFieldCollection, undefined, null);
          expect(expectedResult).toEqual(customFieldCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
