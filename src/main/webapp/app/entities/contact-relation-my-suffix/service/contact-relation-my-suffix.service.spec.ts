import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ContactRelationType } from 'app/entities/enumerations/contact-relation-type.model';
import { IContactRelationMySuffix, ContactRelationMySuffix } from '../contact-relation-my-suffix.model';

import { ContactRelationMySuffixService } from './contact-relation-my-suffix.service';

describe('Service Tests', () => {
  describe('ContactRelationMySuffix Service', () => {
    let service: ContactRelationMySuffixService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactRelationMySuffix;
    let expectedResult: IContactRelationMySuffix | IContactRelationMySuffix[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactRelationMySuffixService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        contactRelationType: ContactRelationType.CUSTOMER,
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

      it('should create a ContactRelationMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContactRelationMySuffix()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactRelationMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            contactRelationType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContactRelationMySuffix', () => {
        const patchObject = Object.assign(
          {
            contactRelationType: 'BBBBBB',
          },
          new ContactRelationMySuffix()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactRelationMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            contactRelationType: 'BBBBBB',
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

      it('should delete a ContactRelationMySuffix', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactRelationMySuffixToCollectionIfMissing', () => {
        it('should add a ContactRelationMySuffix to an empty array', () => {
          const contactRelation: IContactRelationMySuffix = { id: 123 };
          expectedResult = service.addContactRelationMySuffixToCollectionIfMissing([], contactRelation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactRelation);
        });

        it('should not add a ContactRelationMySuffix to an array that contains it', () => {
          const contactRelation: IContactRelationMySuffix = { id: 123 };
          const contactRelationCollection: IContactRelationMySuffix[] = [
            {
              ...contactRelation,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactRelationMySuffixToCollectionIfMissing(contactRelationCollection, contactRelation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactRelationMySuffix to an array that doesn't contain it", () => {
          const contactRelation: IContactRelationMySuffix = { id: 123 };
          const contactRelationCollection: IContactRelationMySuffix[] = [{ id: 456 }];
          expectedResult = service.addContactRelationMySuffixToCollectionIfMissing(contactRelationCollection, contactRelation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactRelation);
        });

        it('should add only unique ContactRelationMySuffix to an array', () => {
          const contactRelationArray: IContactRelationMySuffix[] = [{ id: 123 }, { id: 456 }, { id: 92868 }];
          const contactRelationCollection: IContactRelationMySuffix[] = [{ id: 123 }];
          expectedResult = service.addContactRelationMySuffixToCollectionIfMissing(contactRelationCollection, ...contactRelationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactRelation: IContactRelationMySuffix = { id: 123 };
          const contactRelation2: IContactRelationMySuffix = { id: 456 };
          expectedResult = service.addContactRelationMySuffixToCollectionIfMissing([], contactRelation, contactRelation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactRelation);
          expect(expectedResult).toContain(contactRelation2);
        });

        it('should accept null and undefined values', () => {
          const contactRelation: IContactRelationMySuffix = { id: 123 };
          expectedResult = service.addContactRelationMySuffixToCollectionIfMissing([], null, contactRelation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactRelation);
        });

        it('should return initial array if no ContactRelationMySuffix is added', () => {
          const contactRelationCollection: IContactRelationMySuffix[] = [{ id: 123 }];
          expectedResult = service.addContactRelationMySuffixToCollectionIfMissing(contactRelationCollection, undefined, null);
          expect(expectedResult).toEqual(contactRelationCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
