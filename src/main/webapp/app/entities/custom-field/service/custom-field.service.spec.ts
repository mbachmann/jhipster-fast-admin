import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DomainArea } from 'app/entities/enumerations/domain-area.model';
import { ICustomField, CustomField } from '../custom-field.model';

import { CustomFieldService } from './custom-field.service';

describe('Service Tests', () => {
  describe('CustomField Service', () => {
    let service: CustomFieldService;
    let httpMock: HttpTestingController;
    let elemDefault: ICustomField;
    let expectedResult: ICustomField | ICustomField[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CustomFieldService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        domainArea: DomainArea.CONTACTS,
        key: 'AAAAAAA',
        name: 'AAAAAAA',
        defaultValue: 'AAAAAAA',
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

      it('should create a CustomField', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CustomField()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CustomField', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            domainArea: 'BBBBBB',
            key: 'BBBBBB',
            name: 'BBBBBB',
            defaultValue: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CustomField', () => {
        const patchObject = Object.assign(
          {
            key: 'BBBBBB',
            name: 'BBBBBB',
          },
          new CustomField()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CustomField', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            domainArea: 'BBBBBB',
            key: 'BBBBBB',
            name: 'BBBBBB',
            defaultValue: 'BBBBBB',
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

      it('should delete a CustomField', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCustomFieldToCollectionIfMissing', () => {
        it('should add a CustomField to an empty array', () => {
          const customField: ICustomField = { id: 123 };
          expectedResult = service.addCustomFieldToCollectionIfMissing([], customField);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customField);
        });

        it('should not add a CustomField to an array that contains it', () => {
          const customField: ICustomField = { id: 123 };
          const customFieldCollection: ICustomField[] = [
            {
              ...customField,
            },
            { id: 456 },
          ];
          expectedResult = service.addCustomFieldToCollectionIfMissing(customFieldCollection, customField);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CustomField to an array that doesn't contain it", () => {
          const customField: ICustomField = { id: 123 };
          const customFieldCollection: ICustomField[] = [{ id: 456 }];
          expectedResult = service.addCustomFieldToCollectionIfMissing(customFieldCollection, customField);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customField);
        });

        it('should add only unique CustomField to an array', () => {
          const customFieldArray: ICustomField[] = [{ id: 123 }, { id: 456 }, { id: 94529 }];
          const customFieldCollection: ICustomField[] = [{ id: 123 }];
          expectedResult = service.addCustomFieldToCollectionIfMissing(customFieldCollection, ...customFieldArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const customField: ICustomField = { id: 123 };
          const customField2: ICustomField = { id: 456 };
          expectedResult = service.addCustomFieldToCollectionIfMissing([], customField, customField2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customField);
          expect(expectedResult).toContain(customField2);
        });

        it('should accept null and undefined values', () => {
          const customField: ICustomField = { id: 123 };
          expectedResult = service.addCustomFieldToCollectionIfMissing([], null, customField, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customField);
        });

        it('should return initial array if no CustomField is added', () => {
          const customFieldCollection: ICustomField[] = [{ id: 123 }];
          expectedResult = service.addCustomFieldToCollectionIfMissing(customFieldCollection, undefined, null);
          expect(expectedResult).toEqual(customFieldCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
