import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContactGroup, ContactGroup } from '../contact-group.model';

import { ContactGroupService } from './contact-group.service';

describe('Service Tests', () => {
  describe('ContactGroup Service', () => {
    let service: ContactGroupService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactGroup;
    let expectedResult: IContactGroup | IContactGroup[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactGroupService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        name: 'AAAAAAA',
        usage: 0,
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

      it('should create a ContactGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContactGroup()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            name: 'BBBBBB',
            usage: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContactGroup', () => {
        const patchObject = Object.assign({}, new ContactGroup());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            name: 'BBBBBB',
            usage: 1,
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

      it('should delete a ContactGroup', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactGroupToCollectionIfMissing', () => {
        it('should add a ContactGroup to an empty array', () => {
          const contactGroup: IContactGroup = { id: 123 };
          expectedResult = service.addContactGroupToCollectionIfMissing([], contactGroup);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactGroup);
        });

        it('should not add a ContactGroup to an array that contains it', () => {
          const contactGroup: IContactGroup = { id: 123 };
          const contactGroupCollection: IContactGroup[] = [
            {
              ...contactGroup,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactGroupToCollectionIfMissing(contactGroupCollection, contactGroup);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactGroup to an array that doesn't contain it", () => {
          const contactGroup: IContactGroup = { id: 123 };
          const contactGroupCollection: IContactGroup[] = [{ id: 456 }];
          expectedResult = service.addContactGroupToCollectionIfMissing(contactGroupCollection, contactGroup);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactGroup);
        });

        it('should add only unique ContactGroup to an array', () => {
          const contactGroupArray: IContactGroup[] = [{ id: 123 }, { id: 456 }, { id: 15000 }];
          const contactGroupCollection: IContactGroup[] = [{ id: 123 }];
          expectedResult = service.addContactGroupToCollectionIfMissing(contactGroupCollection, ...contactGroupArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactGroup: IContactGroup = { id: 123 };
          const contactGroup2: IContactGroup = { id: 456 };
          expectedResult = service.addContactGroupToCollectionIfMissing([], contactGroup, contactGroup2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactGroup);
          expect(expectedResult).toContain(contactGroup2);
        });

        it('should accept null and undefined values', () => {
          const contactGroup: IContactGroup = { id: 123 };
          expectedResult = service.addContactGroupToCollectionIfMissing([], null, contactGroup, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactGroup);
        });

        it('should return initial array if no ContactGroup is added', () => {
          const contactGroupCollection: IContactGroup[] = [{ id: 123 }];
          expectedResult = service.addContactGroupToCollectionIfMissing(contactGroupCollection, undefined, null);
          expect(expectedResult).toEqual(contactGroupCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
