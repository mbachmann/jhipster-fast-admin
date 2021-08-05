import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IntervalType } from 'app/entities/enumerations/interval-type.model';
import { IContactReminderMySuffix, ContactReminderMySuffix } from '../contact-reminder-my-suffix.model';

import { ContactReminderMySuffixService } from './contact-reminder-my-suffix.service';

describe('Service Tests', () => {
  describe('ContactReminderMySuffix Service', () => {
    let service: ContactReminderMySuffixService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactReminderMySuffix;
    let expectedResult: IContactReminderMySuffix | IContactReminderMySuffix[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactReminderMySuffixService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        contactId: 0,
        contactName: 'AAAAAAA',
        dateTime: currentDate,
        title: 'AAAAAAA',
        description: 'AAAAAAA',
        intervalValue: 0,
        intervalType: IntervalType.HOUR,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ContactReminderMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateTime: currentDate,
          },
          returnedFromService
        );

        service.create(new ContactReminderMySuffix()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactReminderMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            contactId: 1,
            contactName: 'BBBBBB',
            dateTime: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            description: 'BBBBBB',
            intervalValue: 1,
            intervalType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateTime: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContactReminderMySuffix', () => {
        const patchObject = Object.assign(
          {
            contactId: 1,
            contactName: 'BBBBBB',
            description: 'BBBBBB',
            intervalValue: 1,
          },
          new ContactReminderMySuffix()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateTime: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactReminderMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            contactId: 1,
            contactName: 'BBBBBB',
            dateTime: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            description: 'BBBBBB',
            intervalValue: 1,
            intervalType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateTime: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ContactReminderMySuffix', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactReminderMySuffixToCollectionIfMissing', () => {
        it('should add a ContactReminderMySuffix to an empty array', () => {
          const contactReminder: IContactReminderMySuffix = { id: 123 };
          expectedResult = service.addContactReminderMySuffixToCollectionIfMissing([], contactReminder);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactReminder);
        });

        it('should not add a ContactReminderMySuffix to an array that contains it', () => {
          const contactReminder: IContactReminderMySuffix = { id: 123 };
          const contactReminderCollection: IContactReminderMySuffix[] = [
            {
              ...contactReminder,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactReminderMySuffixToCollectionIfMissing(contactReminderCollection, contactReminder);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactReminderMySuffix to an array that doesn't contain it", () => {
          const contactReminder: IContactReminderMySuffix = { id: 123 };
          const contactReminderCollection: IContactReminderMySuffix[] = [{ id: 456 }];
          expectedResult = service.addContactReminderMySuffixToCollectionIfMissing(contactReminderCollection, contactReminder);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactReminder);
        });

        it('should add only unique ContactReminderMySuffix to an array', () => {
          const contactReminderArray: IContactReminderMySuffix[] = [{ id: 123 }, { id: 456 }, { id: 16628 }];
          const contactReminderCollection: IContactReminderMySuffix[] = [{ id: 123 }];
          expectedResult = service.addContactReminderMySuffixToCollectionIfMissing(contactReminderCollection, ...contactReminderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactReminder: IContactReminderMySuffix = { id: 123 };
          const contactReminder2: IContactReminderMySuffix = { id: 456 };
          expectedResult = service.addContactReminderMySuffixToCollectionIfMissing([], contactReminder, contactReminder2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactReminder);
          expect(expectedResult).toContain(contactReminder2);
        });

        it('should accept null and undefined values', () => {
          const contactReminder: IContactReminderMySuffix = { id: 123 };
          expectedResult = service.addContactReminderMySuffixToCollectionIfMissing([], null, contactReminder, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactReminder);
        });

        it('should return initial array if no ContactReminderMySuffix is added', () => {
          const contactReminderCollection: IContactReminderMySuffix[] = [{ id: 123 }];
          expectedResult = service.addContactReminderMySuffixToCollectionIfMissing(contactReminderCollection, undefined, null);
          expect(expectedResult).toEqual(contactReminderCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
