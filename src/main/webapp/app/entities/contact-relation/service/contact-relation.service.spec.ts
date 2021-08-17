import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ContactRelationType } from 'app/entities/enumerations/contact-relation-type.model';
import { IContactRelation, ContactRelation } from '../contact-relation.model';

import { ContactRelationService } from './contact-relation.service';

describe('Service Tests', () => {
  describe('ContactRelation Service', () => {
    let service: ContactRelationService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactRelation;
    let expectedResult: IContactRelation | IContactRelation[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactRelationService);
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

      it('should create a ContactRelation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContactRelation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactRelation', () => {
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

      it('should partial update a ContactRelation', () => {
        const patchObject = Object.assign(
          {
            contactRelationType: 'BBBBBB',
          },
          new ContactRelation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactRelation', () => {
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

      it('should delete a ContactRelation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactRelationToCollectionIfMissing', () => {
        it('should add a ContactRelation to an empty array', () => {
          const contactRelation: IContactRelation = { id: 123 };
          expectedResult = service.addContactRelationToCollectionIfMissing([], contactRelation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactRelation);
        });

        it('should not add a ContactRelation to an array that contains it', () => {
          const contactRelation: IContactRelation = { id: 123 };
          const contactRelationCollection: IContactRelation[] = [
            {
              ...contactRelation,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactRelationToCollectionIfMissing(contactRelationCollection, contactRelation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactRelation to an array that doesn't contain it", () => {
          const contactRelation: IContactRelation = { id: 123 };
          const contactRelationCollection: IContactRelation[] = [{ id: 456 }];
          expectedResult = service.addContactRelationToCollectionIfMissing(contactRelationCollection, contactRelation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactRelation);
        });

        it('should add only unique ContactRelation to an array', () => {
          const contactRelationArray: IContactRelation[] = [{ id: 123 }, { id: 456 }, { id: 92868 }];
          const contactRelationCollection: IContactRelation[] = [{ id: 123 }];
          expectedResult = service.addContactRelationToCollectionIfMissing(contactRelationCollection, ...contactRelationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactRelation: IContactRelation = { id: 123 };
          const contactRelation2: IContactRelation = { id: 456 };
          expectedResult = service.addContactRelationToCollectionIfMissing([], contactRelation, contactRelation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactRelation);
          expect(expectedResult).toContain(contactRelation2);
        });

        it('should accept null and undefined values', () => {
          const contactRelation: IContactRelation = { id: 123 };
          expectedResult = service.addContactRelationToCollectionIfMissing([], null, contactRelation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactRelation);
        });

        it('should return initial array if no ContactRelation is added', () => {
          const contactRelationCollection: IContactRelation[] = [{ id: 123 }];
          expectedResult = service.addContactRelationToCollectionIfMissing(contactRelationCollection, undefined, null);
          expect(expectedResult).toEqual(contactRelationCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
