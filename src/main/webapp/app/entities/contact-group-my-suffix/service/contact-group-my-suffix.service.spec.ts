import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContactGroupMySuffix, ContactGroupMySuffix } from '../contact-group-my-suffix.model';

import { ContactGroupMySuffixService } from './contact-group-my-suffix.service';

describe('Service Tests', () => {
  describe('ContactGroupMySuffix Service', () => {
    let service: ContactGroupMySuffixService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactGroupMySuffix;
    let expectedResult: IContactGroupMySuffix | IContactGroupMySuffix[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactGroupMySuffixService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a ContactGroupMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContactGroupMySuffix()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactGroupMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContactGroupMySuffix', () => {
        const patchObject = Object.assign({}, new ContactGroupMySuffix());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactGroupMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a ContactGroupMySuffix', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactGroupMySuffixToCollectionIfMissing', () => {
        it('should add a ContactGroupMySuffix to an empty array', () => {
          const contactGroup: IContactGroupMySuffix = { id: 123 };
          expectedResult = service.addContactGroupMySuffixToCollectionIfMissing([], contactGroup);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactGroup);
        });

        it('should not add a ContactGroupMySuffix to an array that contains it', () => {
          const contactGroup: IContactGroupMySuffix = { id: 123 };
          const contactGroupCollection: IContactGroupMySuffix[] = [
            {
              ...contactGroup,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactGroupMySuffixToCollectionIfMissing(contactGroupCollection, contactGroup);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactGroupMySuffix to an array that doesn't contain it", () => {
          const contactGroup: IContactGroupMySuffix = { id: 123 };
          const contactGroupCollection: IContactGroupMySuffix[] = [{ id: 456 }];
          expectedResult = service.addContactGroupMySuffixToCollectionIfMissing(contactGroupCollection, contactGroup);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactGroup);
        });

        it('should add only unique ContactGroupMySuffix to an array', () => {
          const contactGroupArray: IContactGroupMySuffix[] = [{ id: 123 }, { id: 456 }, { id: 26026 }];
          const contactGroupCollection: IContactGroupMySuffix[] = [{ id: 123 }];
          expectedResult = service.addContactGroupMySuffixToCollectionIfMissing(contactGroupCollection, ...contactGroupArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactGroup: IContactGroupMySuffix = { id: 123 };
          const contactGroup2: IContactGroupMySuffix = { id: 456 };
          expectedResult = service.addContactGroupMySuffixToCollectionIfMissing([], contactGroup, contactGroup2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactGroup);
          expect(expectedResult).toContain(contactGroup2);
        });

        it('should accept null and undefined values', () => {
          const contactGroup: IContactGroupMySuffix = { id: 123 };
          expectedResult = service.addContactGroupMySuffixToCollectionIfMissing([], null, contactGroup, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactGroup);
        });

        it('should return initial array if no ContactGroupMySuffix is added', () => {
          const contactGroupCollection: IContactGroupMySuffix[] = [{ id: 123 }];
          expectedResult = service.addContactGroupMySuffixToCollectionIfMissing(contactGroupCollection, undefined, null);
          expect(expectedResult).toEqual(contactGroupCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
