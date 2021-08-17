import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AccountType } from 'app/entities/enumerations/account-type.model';
import { IContactAccount, ContactAccount } from '../contact-account.model';

import { ContactAccountService } from './contact-account.service';

describe('Service Tests', () => {
  describe('ContactAccount Service', () => {
    let service: ContactAccountService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactAccount;
    let expectedResult: IContactAccount | IContactAccount[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactAccountService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        defaultAccount: false,
        type: AccountType.IBAN_NUMBER,
        number: 'AAAAAAA',
        bic: 'AAAAAAA',
        description: 'AAAAAAA',
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

      it('should create a ContactAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContactAccount()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            defaultAccount: true,
            type: 'BBBBBB',
            number: 'BBBBBB',
            bic: 'BBBBBB',
            description: 'BBBBBB',
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

      it('should partial update a ContactAccount', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
            description: 'BBBBBB',
            inactiv: true,
          },
          new ContactAccount()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            defaultAccount: true,
            type: 'BBBBBB',
            number: 'BBBBBB',
            bic: 'BBBBBB',
            description: 'BBBBBB',
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

      it('should delete a ContactAccount', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactAccountToCollectionIfMissing', () => {
        it('should add a ContactAccount to an empty array', () => {
          const contactAccount: IContactAccount = { id: 123 };
          expectedResult = service.addContactAccountToCollectionIfMissing([], contactAccount);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactAccount);
        });

        it('should not add a ContactAccount to an array that contains it', () => {
          const contactAccount: IContactAccount = { id: 123 };
          const contactAccountCollection: IContactAccount[] = [
            {
              ...contactAccount,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactAccountToCollectionIfMissing(contactAccountCollection, contactAccount);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactAccount to an array that doesn't contain it", () => {
          const contactAccount: IContactAccount = { id: 123 };
          const contactAccountCollection: IContactAccount[] = [{ id: 456 }];
          expectedResult = service.addContactAccountToCollectionIfMissing(contactAccountCollection, contactAccount);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactAccount);
        });

        it('should add only unique ContactAccount to an array', () => {
          const contactAccountArray: IContactAccount[] = [{ id: 123 }, { id: 456 }, { id: 60730 }];
          const contactAccountCollection: IContactAccount[] = [{ id: 123 }];
          expectedResult = service.addContactAccountToCollectionIfMissing(contactAccountCollection, ...contactAccountArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactAccount: IContactAccount = { id: 123 };
          const contactAccount2: IContactAccount = { id: 456 };
          expectedResult = service.addContactAccountToCollectionIfMissing([], contactAccount, contactAccount2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactAccount);
          expect(expectedResult).toContain(contactAccount2);
        });

        it('should accept null and undefined values', () => {
          const contactAccount: IContactAccount = { id: 123 };
          expectedResult = service.addContactAccountToCollectionIfMissing([], null, contactAccount, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactAccount);
        });

        it('should return initial array if no ContactAccount is added', () => {
          const contactAccountCollection: IContactAccount[] = [{ id: 123 }];
          expectedResult = service.addContactAccountToCollectionIfMissing(contactAccountCollection, undefined, null);
          expect(expectedResult).toEqual(contactAccountCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
