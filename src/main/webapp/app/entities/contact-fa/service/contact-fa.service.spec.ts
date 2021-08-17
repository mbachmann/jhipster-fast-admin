import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ContactType } from 'app/entities/enumerations/contact-type.model';
import { GenderType } from 'app/entities/enumerations/gender-type.model';
import { CommunicationChannel } from 'app/entities/enumerations/communication-channel.model';
import { CommunicationNewsletter } from 'app/entities/enumerations/communication-newsletter.model';
import { Currency } from 'app/entities/enumerations/currency.model';
import { DiscountType } from 'app/entities/enumerations/discount-type.model';
import { IContactFa, ContactFa } from '../contact-fa.model';

import { ContactFaService } from './contact-fa.service';

describe('Service Tests', () => {
  describe('ContactFa Service', () => {
    let service: ContactFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactFa;
    let expectedResult: IContactFa | IContactFa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactFaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        number: 'AAAAAAA',
        type: ContactType.COMPANY,
        gender: GenderType.MALE,
        genderSalutationActive: false,
        name: 'AAAAAAA',
        nameAddition: 'AAAAAAA',
        salutation: 'AAAAAAA',
        phone: 'AAAAAAA',
        fax: 'AAAAAAA',
        email: 'AAAAAAA',
        website: 'AAAAAAA',
        notes: 'AAAAAAA',
        communicationLanguage: 'AAAAAAA',
        communicationChannel: CommunicationChannel.NO_PREFERENCE,
        communicationNewsletter: CommunicationNewsletter.SEND_ADDRESS_AND_CONTACTS,
        currency: Currency.AED,
        ebillAccountId: 'AAAAAAA',
        vatIdentification: 'AAAAAAA',
        vatRate: 0,
        discountRate: 0,
        discountType: DiscountType.IN_PERCENT,
        paymentGrace: 0,
        hourlyRate: 0,
        created: currentDate,
        mainAddressId: 0,
        birthDate: currentDate,
        birthPlace: 'AAAAAAA',
        placeOfOrigin: 'AAAAAAA',
        citizenCountry1: 'AAAAAAA',
        citizenCountry2: 'AAAAAAA',
        socialSecurityNumber: 'AAAAAAA',
        hobbies: 'AAAAAAA',
        dailyWork: 'AAAAAAA',
        contactAttribute01: 'AAAAAAA',
        avatarContentType: 'image/png',
        avatar: 'AAAAAAA',
        imageType: 'AAAAAAA',
        inactiv: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
            birthDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ContactFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            created: currentDate.format(DATE_TIME_FORMAT),
            birthDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ContactFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            type: 'BBBBBB',
            gender: 'BBBBBB',
            genderSalutationActive: true,
            name: 'BBBBBB',
            nameAddition: 'BBBBBB',
            salutation: 'BBBBBB',
            phone: 'BBBBBB',
            fax: 'BBBBBB',
            email: 'BBBBBB',
            website: 'BBBBBB',
            notes: 'BBBBBB',
            communicationLanguage: 'BBBBBB',
            communicationChannel: 'BBBBBB',
            communicationNewsletter: 'BBBBBB',
            currency: 'BBBBBB',
            ebillAccountId: 'BBBBBB',
            vatIdentification: 'BBBBBB',
            vatRate: 1,
            discountRate: 1,
            discountType: 'BBBBBB',
            paymentGrace: 1,
            hourlyRate: 1,
            created: currentDate.format(DATE_TIME_FORMAT),
            mainAddressId: 1,
            birthDate: currentDate.format(DATE_FORMAT),
            birthPlace: 'BBBBBB',
            placeOfOrigin: 'BBBBBB',
            citizenCountry1: 'BBBBBB',
            citizenCountry2: 'BBBBBB',
            socialSecurityNumber: 'BBBBBB',
            hobbies: 'BBBBBB',
            dailyWork: 'BBBBBB',
            contactAttribute01: 'BBBBBB',
            avatar: 'BBBBBB',
            imageType: 'BBBBBB',
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContactFa', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
            type: 'BBBBBB',
            nameAddition: 'BBBBBB',
            salutation: 'BBBBBB',
            communicationLanguage: 'BBBBBB',
            ebillAccountId: 'BBBBBB',
            vatRate: 1,
            hourlyRate: 1,
            mainAddressId: 1,
            birthDate: currentDate.format(DATE_FORMAT),
            socialSecurityNumber: 'BBBBBB',
            hobbies: 'BBBBBB',
            contactAttribute01: 'BBBBBB',
            avatar: 'BBBBBB',
            imageType: 'BBBBBB',
            inactiv: true,
          },
          new ContactFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            created: currentDate,
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            number: 'BBBBBB',
            type: 'BBBBBB',
            gender: 'BBBBBB',
            genderSalutationActive: true,
            name: 'BBBBBB',
            nameAddition: 'BBBBBB',
            salutation: 'BBBBBB',
            phone: 'BBBBBB',
            fax: 'BBBBBB',
            email: 'BBBBBB',
            website: 'BBBBBB',
            notes: 'BBBBBB',
            communicationLanguage: 'BBBBBB',
            communicationChannel: 'BBBBBB',
            communicationNewsletter: 'BBBBBB',
            currency: 'BBBBBB',
            ebillAccountId: 'BBBBBB',
            vatIdentification: 'BBBBBB',
            vatRate: 1,
            discountRate: 1,
            discountType: 'BBBBBB',
            paymentGrace: 1,
            hourlyRate: 1,
            created: currentDate.format(DATE_TIME_FORMAT),
            mainAddressId: 1,
            birthDate: currentDate.format(DATE_FORMAT),
            birthPlace: 'BBBBBB',
            placeOfOrigin: 'BBBBBB',
            citizenCountry1: 'BBBBBB',
            citizenCountry2: 'BBBBBB',
            socialSecurityNumber: 'BBBBBB',
            hobbies: 'BBBBBB',
            dailyWork: 'BBBBBB',
            contactAttribute01: 'BBBBBB',
            avatar: 'BBBBBB',
            imageType: 'BBBBBB',
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ContactFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactFaToCollectionIfMissing', () => {
        it('should add a ContactFa to an empty array', () => {
          const contact: IContactFa = { id: 123 };
          expectedResult = service.addContactFaToCollectionIfMissing([], contact);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contact);
        });

        it('should not add a ContactFa to an array that contains it', () => {
          const contact: IContactFa = { id: 123 };
          const contactCollection: IContactFa[] = [
            {
              ...contact,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactFaToCollectionIfMissing(contactCollection, contact);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactFa to an array that doesn't contain it", () => {
          const contact: IContactFa = { id: 123 };
          const contactCollection: IContactFa[] = [{ id: 456 }];
          expectedResult = service.addContactFaToCollectionIfMissing(contactCollection, contact);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contact);
        });

        it('should add only unique ContactFa to an array', () => {
          const contactArray: IContactFa[] = [{ id: 123 }, { id: 456 }, { id: 79074 }];
          const contactCollection: IContactFa[] = [{ id: 123 }];
          expectedResult = service.addContactFaToCollectionIfMissing(contactCollection, ...contactArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contact: IContactFa = { id: 123 };
          const contact2: IContactFa = { id: 456 };
          expectedResult = service.addContactFaToCollectionIfMissing([], contact, contact2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contact);
          expect(expectedResult).toContain(contact2);
        });

        it('should accept null and undefined values', () => {
          const contact: IContactFa = { id: 123 };
          expectedResult = service.addContactFaToCollectionIfMissing([], null, contact, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contact);
        });

        it('should return initial array if no ContactFa is added', () => {
          const contactCollection: IContactFa[] = [{ id: 123 }];
          expectedResult = service.addContactFaToCollectionIfMissing(contactCollection, undefined, null);
          expect(expectedResult).toEqual(contactCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
