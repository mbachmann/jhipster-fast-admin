import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ContactType } from 'app/entities/enumerations/contact-type.model';
import { GenderType } from 'app/entities/enumerations/gender-type.model';
import { CommunicationChannel } from 'app/entities/enumerations/communication-channel.model';
import { CommunicationNewsletter } from 'app/entities/enumerations/communication-newsletter.model';
import { DiscountRate } from 'app/entities/enumerations/discount-rate.model';
import { IContactMySuffix, ContactMySuffix } from '../contact-my-suffix.model';

import { ContactMySuffixService } from './contact-my-suffix.service';

describe('Service Tests', () => {
  describe('ContactMySuffix Service', () => {
    let service: ContactMySuffixService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactMySuffix;
    let expectedResult: IContactMySuffix | IContactMySuffix[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactMySuffixService);
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
        currency: 'AAAAAAA',
        ebillAccountId: 'AAAAAAA',
        vatIdentification: 'AAAAAAA',
        vatRate: 0,
        discountRate: 0,
        discountType: DiscountRate.IN_PERCENT,
        paymentGrace: 0,
        hourlyRate: 0,
        created: currentDate,
        mainAddressId: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ContactMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
          },
          returnedFromService
        );

        service.create(new ContactMySuffix()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactMySuffix', () => {
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
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContactMySuffix', () => {
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
          },
          new ContactMySuffix()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            created: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactMySuffix', () => {
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
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ContactMySuffix', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactMySuffixToCollectionIfMissing', () => {
        it('should add a ContactMySuffix to an empty array', () => {
          const contact: IContactMySuffix = { id: 123 };
          expectedResult = service.addContactMySuffixToCollectionIfMissing([], contact);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contact);
        });

        it('should not add a ContactMySuffix to an array that contains it', () => {
          const contact: IContactMySuffix = { id: 123 };
          const contactCollection: IContactMySuffix[] = [
            {
              ...contact,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactMySuffixToCollectionIfMissing(contactCollection, contact);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactMySuffix to an array that doesn't contain it", () => {
          const contact: IContactMySuffix = { id: 123 };
          const contactCollection: IContactMySuffix[] = [{ id: 456 }];
          expectedResult = service.addContactMySuffixToCollectionIfMissing(contactCollection, contact);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contact);
        });

        it('should add only unique ContactMySuffix to an array', () => {
          const contactArray: IContactMySuffix[] = [{ id: 123 }, { id: 456 }, { id: 52114 }];
          const contactCollection: IContactMySuffix[] = [{ id: 123 }];
          expectedResult = service.addContactMySuffixToCollectionIfMissing(contactCollection, ...contactArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contact: IContactMySuffix = { id: 123 };
          const contact2: IContactMySuffix = { id: 456 };
          expectedResult = service.addContactMySuffixToCollectionIfMissing([], contact, contact2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contact);
          expect(expectedResult).toContain(contact2);
        });

        it('should accept null and undefined values', () => {
          const contact: IContactMySuffix = { id: 123 };
          expectedResult = service.addContactMySuffixToCollectionIfMissing([], null, contact, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contact);
        });

        it('should return initial array if no ContactMySuffix is added', () => {
          const contactCollection: IContactMySuffix[] = [{ id: 123 }];
          expectedResult = service.addContactMySuffixToCollectionIfMissing(contactCollection, undefined, null);
          expect(expectedResult).toEqual(contactCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
