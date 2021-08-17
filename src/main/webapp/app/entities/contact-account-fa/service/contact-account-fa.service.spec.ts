import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AccountType } from 'app/entities/enumerations/account-type.model';
import { IContactAccountFa, ContactAccountFa } from '../contact-account-fa.model';

import { ContactAccountFaService } from './contact-account-fa.service';

describe('Service Tests', () => {
  describe('ContactAccountFa Service', () => {
    let service: ContactAccountFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactAccountFa;
    let expectedResult: IContactAccountFa | IContactAccountFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactAccountFaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        defaultAccount: false,
        type: AccountType.IBAN,
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

      it('should create a ContactAccountFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContactAccountFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactAccountFa', () => {
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

      it('should partial update a ContactAccountFa', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
            description: 'BBBBBB',
            inactiv: true,
          },
          new ContactAccountFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactAccountFa', () => {
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

      it('should delete a ContactAccountFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactAccountFaToCollectionIfMissing', () => {
        it('should add a ContactAccountFa to an empty array', () => {
          const contactAccount: IContactAccountFa = { id: 123 };
          expectedResult = service.addContactAccountFaToCollectionIfMissing([], contactAccount);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactAccount);
        });

        it('should not add a ContactAccountFa to an array that contains it', () => {
          const contactAccount: IContactAccountFa = { id: 123 };
          const contactAccountCollection: IContactAccountFa[] = [
            {
              ...contactAccount,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactAccountFaToCollectionIfMissing(contactAccountCollection, contactAccount);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactAccountFa to an array that doesn't contain it", () => {
          const contactAccount: IContactAccountFa = { id: 123 };
          const contactAccountCollection: IContactAccountFa[] = [{ id: 456 }];
          expectedResult = service.addContactAccountFaToCollectionIfMissing(contactAccountCollection, contactAccount);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactAccount);
        });

        it('should add only unique ContactAccountFa to an array', () => {
          const contactAccountArray: IContactAccountFa[] = [{ id: 123 }, { id: 456 }, { id: 60730 }];
          const contactAccountCollection: IContactAccountFa[] = [{ id: 123 }];
          expectedResult = service.addContactAccountFaToCollectionIfMissing(contactAccountCollection, ...contactAccountArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactAccount: IContactAccountFa = { id: 123 };
          const contactAccount2: IContactAccountFa = { id: 456 };
          expectedResult = service.addContactAccountFaToCollectionIfMissing([], contactAccount, contactAccount2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactAccount);
          expect(expectedResult).toContain(contactAccount2);
        });

        it('should accept null and undefined values', () => {
          const contactAccount: IContactAccountFa = { id: 123 };
          expectedResult = service.addContactAccountFaToCollectionIfMissing([], null, contactAccount, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactAccount);
        });

        it('should return initial array if no ContactAccountFa is added', () => {
          const contactAccountCollection: IContactAccountFa[] = [{ id: 123 }];
          expectedResult = service.addContactAccountFaToCollectionIfMissing(contactAccountCollection, undefined, null);
          expect(expectedResult).toEqual(contactAccountCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
