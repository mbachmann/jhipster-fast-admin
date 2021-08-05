import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ContactRelationType } from 'app/entities/enumerations/contact-relation-type.model';
import { IContactRelationFa, ContactRelationFa } from '../contact-relation-fa.model';

import { ContactRelationFaService } from './contact-relation-fa.service';

describe('Service Tests', () => {
  describe('ContactRelationFa Service', () => {
    let service: ContactRelationFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactRelationFa;
    let expectedResult: IContactRelationFa | IContactRelationFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactRelationFaService);
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

      it('should create a ContactRelationFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContactRelationFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactRelationFa', () => {
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

      it('should partial update a ContactRelationFa', () => {
        const patchObject = Object.assign(
          {
            contactRelationType: 'BBBBBB',
          },
          new ContactRelationFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactRelationFa', () => {
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

      it('should delete a ContactRelationFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactRelationFaToCollectionIfMissing', () => {
        it('should add a ContactRelationFa to an empty array', () => {
          const contactRelation: IContactRelationFa = { id: 123 };
          expectedResult = service.addContactRelationFaToCollectionIfMissing([], contactRelation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactRelation);
        });

        it('should not add a ContactRelationFa to an array that contains it', () => {
          const contactRelation: IContactRelationFa = { id: 123 };
          const contactRelationCollection: IContactRelationFa[] = [
            {
              ...contactRelation,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactRelationFaToCollectionIfMissing(contactRelationCollection, contactRelation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactRelationFa to an array that doesn't contain it", () => {
          const contactRelation: IContactRelationFa = { id: 123 };
          const contactRelationCollection: IContactRelationFa[] = [{ id: 456 }];
          expectedResult = service.addContactRelationFaToCollectionIfMissing(contactRelationCollection, contactRelation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactRelation);
        });

        it('should add only unique ContactRelationFa to an array', () => {
          const contactRelationArray: IContactRelationFa[] = [{ id: 123 }, { id: 456 }, { id: 92868 }];
          const contactRelationCollection: IContactRelationFa[] = [{ id: 123 }];
          expectedResult = service.addContactRelationFaToCollectionIfMissing(contactRelationCollection, ...contactRelationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactRelation: IContactRelationFa = { id: 123 };
          const contactRelation2: IContactRelationFa = { id: 456 };
          expectedResult = service.addContactRelationFaToCollectionIfMissing([], contactRelation, contactRelation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactRelation);
          expect(expectedResult).toContain(contactRelation2);
        });

        it('should accept null and undefined values', () => {
          const contactRelation: IContactRelationFa = { id: 123 };
          expectedResult = service.addContactRelationFaToCollectionIfMissing([], null, contactRelation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactRelation);
        });

        it('should return initial array if no ContactRelationFa is added', () => {
          const contactRelationCollection: IContactRelationFa[] = [{ id: 123 }];
          expectedResult = service.addContactRelationFaToCollectionIfMissing(contactRelationCollection, undefined, null);
          expect(expectedResult).toEqual(contactRelationCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
