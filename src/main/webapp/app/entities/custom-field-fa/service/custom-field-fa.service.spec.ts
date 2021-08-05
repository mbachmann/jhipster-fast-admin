import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DomainArea } from 'app/entities/enumerations/domain-area.model';
import { ICustomFieldFa, CustomFieldFa } from '../custom-field-fa.model';

import { CustomFieldFaService } from './custom-field-fa.service';

describe('Service Tests', () => {
  describe('CustomFieldFa Service', () => {
    let service: CustomFieldFaService;
    let httpMock: HttpTestingController;
    let elemDefault: ICustomFieldFa;
    let expectedResult: ICustomFieldFa | ICustomFieldFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CustomFieldFaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        domainArea: DomainArea.AFFILIATE,
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

      it('should create a CustomFieldFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CustomFieldFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CustomFieldFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            domainArea: 'BBBBBB',
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

      it('should partial update a CustomFieldFa', () => {
        const patchObject = Object.assign(
          {
            key: 'BBBBBB',
            name: 'BBBBBB',
          },
          new CustomFieldFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CustomFieldFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            domainArea: 'BBBBBB',
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

      it('should delete a CustomFieldFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCustomFieldFaToCollectionIfMissing', () => {
        it('should add a CustomFieldFa to an empty array', () => {
          const customField: ICustomFieldFa = { id: 123 };
          expectedResult = service.addCustomFieldFaToCollectionIfMissing([], customField);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customField);
        });

        it('should not add a CustomFieldFa to an array that contains it', () => {
          const customField: ICustomFieldFa = { id: 123 };
          const customFieldCollection: ICustomFieldFa[] = [
            {
              ...customField,
            },
            { id: 456 },
          ];
          expectedResult = service.addCustomFieldFaToCollectionIfMissing(customFieldCollection, customField);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CustomFieldFa to an array that doesn't contain it", () => {
          const customField: ICustomFieldFa = { id: 123 };
          const customFieldCollection: ICustomFieldFa[] = [{ id: 456 }];
          expectedResult = service.addCustomFieldFaToCollectionIfMissing(customFieldCollection, customField);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customField);
        });

        it('should add only unique CustomFieldFa to an array', () => {
          const customFieldArray: ICustomFieldFa[] = [{ id: 123 }, { id: 456 }, { id: 94529 }];
          const customFieldCollection: ICustomFieldFa[] = [{ id: 123 }];
          expectedResult = service.addCustomFieldFaToCollectionIfMissing(customFieldCollection, ...customFieldArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const customField: ICustomFieldFa = { id: 123 };
          const customField2: ICustomFieldFa = { id: 456 };
          expectedResult = service.addCustomFieldFaToCollectionIfMissing([], customField, customField2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customField);
          expect(expectedResult).toContain(customField2);
        });

        it('should accept null and undefined values', () => {
          const customField: ICustomFieldFa = { id: 123 };
          expectedResult = service.addCustomFieldFaToCollectionIfMissing([], null, customField, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customField);
        });

        it('should return initial array if no CustomFieldFa is added', () => {
          const customFieldCollection: ICustomFieldFa[] = [{ id: 123 }];
          expectedResult = service.addCustomFieldFaToCollectionIfMissing(customFieldCollection, undefined, null);
          expect(expectedResult).toEqual(customFieldCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
