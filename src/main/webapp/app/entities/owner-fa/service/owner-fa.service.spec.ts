import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { CompanyLanguage } from 'app/entities/enumerations/company-language.model';
import { Country } from 'app/entities/enumerations/country.model';
import { CompanyCurrency } from 'app/entities/enumerations/company-currency.model';
import { IOwnerFa, OwnerFa } from '../owner-fa.model';

import { OwnerFaService } from './owner-fa.service';

describe('Service Tests', () => {
  describe('OwnerFa Service', () => {
    let service: OwnerFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IOwnerFa;
    let expectedResult: IOwnerFa | IOwnerFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OwnerFaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        name: 'AAAAAAA',
        surname: 'AAAAAAA',
        email: 'AAAAAAA',
        language: CompanyLanguage.FRENCH,
        companyName: 'AAAAAAA',
        companyAddition: 'AAAAAAA',
        companyCountry: Country.AD,
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
        companyCurrency: CompanyCurrency.CHF,
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

      it('should create a OwnerFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OwnerFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OwnerFa', () => {
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

      it('should partial update a OwnerFa', () => {
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
          new OwnerFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OwnerFa', () => {
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

      it('should delete a OwnerFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOwnerFaToCollectionIfMissing', () => {
        it('should add a OwnerFa to an empty array', () => {
          const owner: IOwnerFa = { id: 123 };
          expectedResult = service.addOwnerFaToCollectionIfMissing([], owner);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(owner);
        });

        it('should not add a OwnerFa to an array that contains it', () => {
          const owner: IOwnerFa = { id: 123 };
          const ownerCollection: IOwnerFa[] = [
            {
              ...owner,
            },
            { id: 456 },
          ];
          expectedResult = service.addOwnerFaToCollectionIfMissing(ownerCollection, owner);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OwnerFa to an array that doesn't contain it", () => {
          const owner: IOwnerFa = { id: 123 };
          const ownerCollection: IOwnerFa[] = [{ id: 456 }];
          expectedResult = service.addOwnerFaToCollectionIfMissing(ownerCollection, owner);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(owner);
        });

        it('should add only unique OwnerFa to an array', () => {
          const ownerArray: IOwnerFa[] = [{ id: 123 }, { id: 456 }, { id: 49916 }];
          const ownerCollection: IOwnerFa[] = [{ id: 123 }];
          expectedResult = service.addOwnerFaToCollectionIfMissing(ownerCollection, ...ownerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const owner: IOwnerFa = { id: 123 };
          const owner2: IOwnerFa = { id: 456 };
          expectedResult = service.addOwnerFaToCollectionIfMissing([], owner, owner2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(owner);
          expect(expectedResult).toContain(owner2);
        });

        it('should accept null and undefined values', () => {
          const owner: IOwnerFa = { id: 123 };
          expectedResult = service.addOwnerFaToCollectionIfMissing([], null, owner, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(owner);
        });

        it('should return initial array if no OwnerFa is added', () => {
          const ownerCollection: IOwnerFa[] = [{ id: 123 }];
          expectedResult = service.addOwnerFaToCollectionIfMissing(ownerCollection, undefined, null);
          expect(expectedResult).toEqual(ownerCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
