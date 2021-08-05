import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { LanguageType } from 'app/entities/enumerations/language-type.model';
import { CurrencyType } from 'app/entities/enumerations/currency-type.model';
import { IOwnerMySuffix, OwnerMySuffix } from '../owner-my-suffix.model';

import { OwnerMySuffixService } from './owner-my-suffix.service';

describe('Service Tests', () => {
  describe('OwnerMySuffix Service', () => {
    let service: OwnerMySuffixService;
    let httpMock: HttpTestingController;
    let elemDefault: IOwnerMySuffix;
    let expectedResult: IOwnerMySuffix | IOwnerMySuffix[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OwnerMySuffixService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        name: 'AAAAAAA',
        surname: 'AAAAAAA',
        email: 'AAAAAAA',
        language: LanguageType.FRENCH,
        companyName: 'AAAAAAA',
        companyAddition: 'AAAAAAA',
        companyCountry: 'AAAAAAA',
        companyStreet: 'AAAAAAA',
        companyStreetNo: 'AAAAAAA',
        companyStreet2: 'AAAAAAA',
        companyPostcode: 'AAAAAAA',
        companyCity: 'AAAAAAA',
        companyPhone: 'AAAAAAA',
        companyFax: 'AAAAAAA',
        companyEmail: 'AAAAAAA',
        companyWebsite: 'AAAAAAA',
        companyVatId: 'AAAAAAA',
        companyCurrency: CurrencyType.CHF,
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

      it('should create a OwnerMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OwnerMySuffix()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OwnerMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            name: 'BBBBBB',
            surname: 'BBBBBB',
            email: 'BBBBBB',
            language: 'BBBBBB',
            companyName: 'BBBBBB',
            companyAddition: 'BBBBBB',
            companyCountry: 'BBBBBB',
            companyStreet: 'BBBBBB',
            companyStreetNo: 'BBBBBB',
            companyStreet2: 'BBBBBB',
            companyPostcode: 'BBBBBB',
            companyCity: 'BBBBBB',
            companyPhone: 'BBBBBB',
            companyFax: 'BBBBBB',
            companyEmail: 'BBBBBB',
            companyWebsite: 'BBBBBB',
            companyVatId: 'BBBBBB',
            companyCurrency: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OwnerMySuffix', () => {
        const patchObject = Object.assign(
          {
            surname: 'BBBBBB',
            language: 'BBBBBB',
            companyName: 'BBBBBB',
            companyAddition: 'BBBBBB',
            companyCountry: 'BBBBBB',
            companyStreetNo: 'BBBBBB',
            companyPhone: 'BBBBBB',
            companyEmail: 'BBBBBB',
            companyWebsite: 'BBBBBB',
            companyCurrency: 'BBBBBB',
          },
          new OwnerMySuffix()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OwnerMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            name: 'BBBBBB',
            surname: 'BBBBBB',
            email: 'BBBBBB',
            language: 'BBBBBB',
            companyName: 'BBBBBB',
            companyAddition: 'BBBBBB',
            companyCountry: 'BBBBBB',
            companyStreet: 'BBBBBB',
            companyStreetNo: 'BBBBBB',
            companyStreet2: 'BBBBBB',
            companyPostcode: 'BBBBBB',
            companyCity: 'BBBBBB',
            companyPhone: 'BBBBBB',
            companyFax: 'BBBBBB',
            companyEmail: 'BBBBBB',
            companyWebsite: 'BBBBBB',
            companyVatId: 'BBBBBB',
            companyCurrency: 'BBBBBB',
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

      it('should delete a OwnerMySuffix', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOwnerMySuffixToCollectionIfMissing', () => {
        it('should add a OwnerMySuffix to an empty array', () => {
          const owner: IOwnerMySuffix = { id: 123 };
          expectedResult = service.addOwnerMySuffixToCollectionIfMissing([], owner);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(owner);
        });

        it('should not add a OwnerMySuffix to an array that contains it', () => {
          const owner: IOwnerMySuffix = { id: 123 };
          const ownerCollection: IOwnerMySuffix[] = [
            {
              ...owner,
            },
            { id: 456 },
          ];
          expectedResult = service.addOwnerMySuffixToCollectionIfMissing(ownerCollection, owner);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OwnerMySuffix to an array that doesn't contain it", () => {
          const owner: IOwnerMySuffix = { id: 123 };
          const ownerCollection: IOwnerMySuffix[] = [{ id: 456 }];
          expectedResult = service.addOwnerMySuffixToCollectionIfMissing(ownerCollection, owner);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(owner);
        });

        it('should add only unique OwnerMySuffix to an array', () => {
          const ownerArray: IOwnerMySuffix[] = [{ id: 123 }, { id: 456 }, { id: 49916 }];
          const ownerCollection: IOwnerMySuffix[] = [{ id: 123 }];
          expectedResult = service.addOwnerMySuffixToCollectionIfMissing(ownerCollection, ...ownerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const owner: IOwnerMySuffix = { id: 123 };
          const owner2: IOwnerMySuffix = { id: 456 };
          expectedResult = service.addOwnerMySuffixToCollectionIfMissing([], owner, owner2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(owner);
          expect(expectedResult).toContain(owner2);
        });

        it('should accept null and undefined values', () => {
          const owner: IOwnerMySuffix = { id: 123 };
          expectedResult = service.addOwnerMySuffixToCollectionIfMissing([], null, owner, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(owner);
        });

        it('should return initial array if no OwnerMySuffix is added', () => {
          const ownerCollection: IOwnerMySuffix[] = [{ id: 123 }];
          expectedResult = service.addOwnerMySuffixToCollectionIfMissing(ownerCollection, undefined, null);
          expect(expectedResult).toEqual(ownerCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
