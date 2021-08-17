import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AutoSynch } from 'app/entities/enumerations/auto-synch.model';
import { AutoSynchDirection } from 'app/entities/enumerations/auto-synch-direction.model';
import { Currency } from 'app/entities/enumerations/currency.model';
import { IBankAccountFa, BankAccountFa } from '../bank-account-fa.model';

import { BankAccountFaService } from './bank-account-fa.service';

describe('Service Tests', () => {
  describe('BankAccountFa Service', () => {
    let service: BankAccountFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IBankAccountFa;
    let expectedResult: IBankAccountFa | IBankAccountFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BankAccountFaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        defaultBankAccount: false,
        description: 'AAAAAAA',
        bankName: 'AAAAAAA',
        number: 'AAAAAAA',
        iban: 'AAAAAAA',
        bic: 'AAAAAAA',
        postAccount: 'AAAAAAA',
        autoSync: AutoSynch.ACTIVE,
        autoSyncDirection: AutoSynchDirection.IN,
        currency: Currency.AED,
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

      it('should create a BankAccountFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new BankAccountFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BankAccountFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            defaultBankAccount: true,
            description: 'BBBBBB',
            bankName: 'BBBBBB',
            number: 'BBBBBB',
            iban: 'BBBBBB',
            bic: 'BBBBBB',
            postAccount: 'BBBBBB',
            autoSync: 'BBBBBB',
            autoSyncDirection: 'BBBBBB',
            currency: 'BBBBBB',
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

      it('should partial update a BankAccountFa', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
            iban: 'BBBBBB',
            postAccount: 'BBBBBB',
            autoSync: 'BBBBBB',
            currency: 'BBBBBB',
            inactiv: true,
          },
          new BankAccountFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BankAccountFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            defaultBankAccount: true,
            description: 'BBBBBB',
            bankName: 'BBBBBB',
            number: 'BBBBBB',
            iban: 'BBBBBB',
            bic: 'BBBBBB',
            postAccount: 'BBBBBB',
            autoSync: 'BBBBBB',
            autoSyncDirection: 'BBBBBB',
            currency: 'BBBBBB',
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

      it('should delete a BankAccountFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBankAccountFaToCollectionIfMissing', () => {
        it('should add a BankAccountFa to an empty array', () => {
          const bankAccount: IBankAccountFa = { id: 123 };
          expectedResult = service.addBankAccountFaToCollectionIfMissing([], bankAccount);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bankAccount);
        });

        it('should not add a BankAccountFa to an array that contains it', () => {
          const bankAccount: IBankAccountFa = { id: 123 };
          const bankAccountCollection: IBankAccountFa[] = [
            {
              ...bankAccount,
            },
            { id: 456 },
          ];
          expectedResult = service.addBankAccountFaToCollectionIfMissing(bankAccountCollection, bankAccount);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BankAccountFa to an array that doesn't contain it", () => {
          const bankAccount: IBankAccountFa = { id: 123 };
          const bankAccountCollection: IBankAccountFa[] = [{ id: 456 }];
          expectedResult = service.addBankAccountFaToCollectionIfMissing(bankAccountCollection, bankAccount);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bankAccount);
        });

        it('should add only unique BankAccountFa to an array', () => {
          const bankAccountArray: IBankAccountFa[] = [{ id: 123 }, { id: 456 }, { id: 82172 }];
          const bankAccountCollection: IBankAccountFa[] = [{ id: 123 }];
          expectedResult = service.addBankAccountFaToCollectionIfMissing(bankAccountCollection, ...bankAccountArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const bankAccount: IBankAccountFa = { id: 123 };
          const bankAccount2: IBankAccountFa = { id: 456 };
          expectedResult = service.addBankAccountFaToCollectionIfMissing([], bankAccount, bankAccount2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bankAccount);
          expect(expectedResult).toContain(bankAccount2);
        });

        it('should accept null and undefined values', () => {
          const bankAccount: IBankAccountFa = { id: 123 };
          expectedResult = service.addBankAccountFaToCollectionIfMissing([], null, bankAccount, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bankAccount);
        });

        it('should return initial array if no BankAccountFa is added', () => {
          const bankAccountCollection: IBankAccountFa[] = [{ id: 123 }];
          expectedResult = service.addBankAccountFaToCollectionIfMissing(bankAccountCollection, undefined, null);
          expect(expectedResult).toEqual(bankAccountCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
