import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContactAddressFa, ContactAddressFa } from '../contact-address-fa.model';

import { ContactAddressFaService } from './contact-address-fa.service';

describe('Service Tests', () => {
  describe('ContactAddressFa Service', () => {
    let service: ContactAddressFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactAddressFa;
    let expectedResult: IContactAddressFa | IContactAddressFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactAddressFaService);
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

      it('should create a ContactAddressFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContactAddressFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactAddressFa', () => {
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

      it('should partial update a ContactAddressFa', () => {
        const patchObject = Object.assign(
          {
            defaultAddress: true,
            street: 'BBBBBB',
            defaultPrepage: true,
          },
          new ContactAddressFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactAddressFa', () => {
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

      it('should delete a ContactAddressFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactAddressFaToCollectionIfMissing', () => {
        it('should add a ContactAddressFa to an empty array', () => {
          const contactAddress: IContactAddressFa = { id: 123 };
          expectedResult = service.addContactAddressFaToCollectionIfMissing([], contactAddress);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactAddress);
        });

        it('should not add a ContactAddressFa to an array that contains it', () => {
          const contactAddress: IContactAddressFa = { id: 123 };
          const contactAddressCollection: IContactAddressFa[] = [
            {
              ...contactAddress,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactAddressFaToCollectionIfMissing(contactAddressCollection, contactAddress);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactAddressFa to an array that doesn't contain it", () => {
          const contactAddress: IContactAddressFa = { id: 123 };
          const contactAddressCollection: IContactAddressFa[] = [{ id: 456 }];
          expectedResult = service.addContactAddressFaToCollectionIfMissing(contactAddressCollection, contactAddress);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactAddress);
        });

        it('should add only unique ContactAddressFa to an array', () => {
          const contactAddressArray: IContactAddressFa[] = [{ id: 123 }, { id: 456 }, { id: 33155 }];
          const contactAddressCollection: IContactAddressFa[] = [{ id: 123 }];
          expectedResult = service.addContactAddressFaToCollectionIfMissing(contactAddressCollection, ...contactAddressArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactAddress: IContactAddressFa = { id: 123 };
          const contactAddress2: IContactAddressFa = { id: 456 };
          expectedResult = service.addContactAddressFaToCollectionIfMissing([], contactAddress, contactAddress2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactAddress);
          expect(expectedResult).toContain(contactAddress2);
        });

        it('should accept null and undefined values', () => {
          const contactAddress: IContactAddressFa = { id: 123 };
          expectedResult = service.addContactAddressFaToCollectionIfMissing([], null, contactAddress, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactAddress);
        });

        it('should return initial array if no ContactAddressFa is added', () => {
          const contactAddressCollection: IContactAddressFa[] = [{ id: 123 }];
          expectedResult = service.addContactAddressFaToCollectionIfMissing(contactAddressCollection, undefined, null);
          expect(expectedResult).toEqual(contactAddressCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
