import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Country } from 'app/entities/enumerations/country.model';
import { IContactAddress, ContactAddress } from '../contact-address.model';

import { ContactAddressService } from './contact-address.service';

describe('Service Tests', () => {
  describe('ContactAddress Service', () => {
    let service: ContactAddressService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactAddress;
    let expectedResult: IContactAddress | IContactAddress[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactAddressService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        defaultAddress: false,
        country: Country.AD,
        street: 'AAAAAAA',
        streetNo: 'AAAAAAA',
        street2: 'AAAAAAA',
        postcode: 'AAAAAAA',
        city: 'AAAAAAA',
        hideOnDocuments: false,
        defaultPrepage: false,
        inactiv: false,
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

      it('should create a ContactAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContactAddress()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactAddress', () => {
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
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContactAddress', () => {
        const patchObject = Object.assign(
          {
            defaultAddress: true,
            street: 'BBBBBB',
            defaultPrepage: true,
          },
          new ContactAddress()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactAddress', () => {
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
            inactiv: true,
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

      it('should delete a ContactAddress', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactAddressToCollectionIfMissing', () => {
        it('should add a ContactAddress to an empty array', () => {
          const contactAddress: IContactAddress = { id: 123 };
          expectedResult = service.addContactAddressToCollectionIfMissing([], contactAddress);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactAddress);
        });

        it('should not add a ContactAddress to an array that contains it', () => {
          const contactAddress: IContactAddress = { id: 123 };
          const contactAddressCollection: IContactAddress[] = [
            {
              ...contactAddress,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactAddressToCollectionIfMissing(contactAddressCollection, contactAddress);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactAddress to an array that doesn't contain it", () => {
          const contactAddress: IContactAddress = { id: 123 };
          const contactAddressCollection: IContactAddress[] = [{ id: 456 }];
          expectedResult = service.addContactAddressToCollectionIfMissing(contactAddressCollection, contactAddress);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactAddress);
        });

        it('should add only unique ContactAddress to an array', () => {
          const contactAddressArray: IContactAddress[] = [{ id: 123 }, { id: 456 }, { id: 54402 }];
          const contactAddressCollection: IContactAddress[] = [{ id: 123 }];
          expectedResult = service.addContactAddressToCollectionIfMissing(contactAddressCollection, ...contactAddressArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactAddress: IContactAddress = { id: 123 };
          const contactAddress2: IContactAddress = { id: 456 };
          expectedResult = service.addContactAddressToCollectionIfMissing([], contactAddress, contactAddress2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactAddress);
          expect(expectedResult).toContain(contactAddress2);
        });

        it('should accept null and undefined values', () => {
          const contactAddress: IContactAddress = { id: 123 };
          expectedResult = service.addContactAddressToCollectionIfMissing([], null, contactAddress, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactAddress);
        });

        it('should return initial array if no ContactAddress is added', () => {
          const contactAddressCollection: IContactAddress[] = [{ id: 123 }];
          expectedResult = service.addContactAddressToCollectionIfMissing(contactAddressCollection, undefined, null);
          expect(expectedResult).toEqual(contactAddressCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
