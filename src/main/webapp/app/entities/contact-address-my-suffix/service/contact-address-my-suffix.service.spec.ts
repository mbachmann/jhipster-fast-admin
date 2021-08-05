import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContactAddressMySuffix, ContactAddressMySuffix } from '../contact-address-my-suffix.model';

import { ContactAddressMySuffixService } from './contact-address-my-suffix.service';

describe('Service Tests', () => {
  describe('ContactAddressMySuffix Service', () => {
    let service: ContactAddressMySuffixService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactAddressMySuffix;
    let expectedResult: IContactAddressMySuffix | IContactAddressMySuffix[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactAddressMySuffixService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        defaultAddress: false,
        country: 'AAAAAAA',
        street: 'AAAAAAA',
        streetNo: 'AAAAAAA',
        street2: 'AAAAAAA',
        postcode: 'AAAAAAA',
        city: 'AAAAAAA',
        hideOnDocuments: false,
        defaultPrepage: false,
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

      it('should create a ContactAddressMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContactAddressMySuffix()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactAddressMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            defaultAddress: true,
            country: 'BBBBBB',
            street: 'BBBBBB',
            streetNo: 'BBBBBB',
            street2: 'BBBBBB',
            postcode: 'BBBBBB',
            city: 'BBBBBB',
            hideOnDocuments: true,
            defaultPrepage: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContactAddressMySuffix', () => {
        const patchObject = Object.assign(
          {
            defaultAddress: true,
            street: 'BBBBBB',
            defaultPrepage: true,
          },
          new ContactAddressMySuffix()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactAddressMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            defaultAddress: true,
            country: 'BBBBBB',
            street: 'BBBBBB',
            streetNo: 'BBBBBB',
            street2: 'BBBBBB',
            postcode: 'BBBBBB',
            city: 'BBBBBB',
            hideOnDocuments: true,
            defaultPrepage: true,
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

      it('should delete a ContactAddressMySuffix', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactAddressMySuffixToCollectionIfMissing', () => {
        it('should add a ContactAddressMySuffix to an empty array', () => {
          const contactAddress: IContactAddressMySuffix = { id: 123 };
          expectedResult = service.addContactAddressMySuffixToCollectionIfMissing([], contactAddress);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactAddress);
        });

        it('should not add a ContactAddressMySuffix to an array that contains it', () => {
          const contactAddress: IContactAddressMySuffix = { id: 123 };
          const contactAddressCollection: IContactAddressMySuffix[] = [
            {
              ...contactAddress,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactAddressMySuffixToCollectionIfMissing(contactAddressCollection, contactAddress);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactAddressMySuffix to an array that doesn't contain it", () => {
          const contactAddress: IContactAddressMySuffix = { id: 123 };
          const contactAddressCollection: IContactAddressMySuffix[] = [{ id: 456 }];
          expectedResult = service.addContactAddressMySuffixToCollectionIfMissing(contactAddressCollection, contactAddress);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactAddress);
        });

        it('should add only unique ContactAddressMySuffix to an array', () => {
          const contactAddressArray: IContactAddressMySuffix[] = [{ id: 123 }, { id: 456 }, { id: 33155 }];
          const contactAddressCollection: IContactAddressMySuffix[] = [{ id: 123 }];
          expectedResult = service.addContactAddressMySuffixToCollectionIfMissing(contactAddressCollection, ...contactAddressArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactAddress: IContactAddressMySuffix = { id: 123 };
          const contactAddress2: IContactAddressMySuffix = { id: 456 };
          expectedResult = service.addContactAddressMySuffixToCollectionIfMissing([], contactAddress, contactAddress2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactAddress);
          expect(expectedResult).toContain(contactAddress2);
        });

        it('should accept null and undefined values', () => {
          const contactAddress: IContactAddressMySuffix = { id: 123 };
          expectedResult = service.addContactAddressMySuffixToCollectionIfMissing([], null, contactAddress, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactAddress);
        });

        it('should return initial array if no ContactAddressMySuffix is added', () => {
          const contactAddressCollection: IContactAddressMySuffix[] = [{ id: 123 }];
          expectedResult = service.addContactAddressMySuffixToCollectionIfMissing(contactAddressCollection, undefined, null);
          expect(expectedResult).toEqual(contactAddressCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
